package dev.proxy.service;

import dev.proxy.client.DummyJSONClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

import dev.proxy.model.cache.CachedResponse;

@ApplicationScoped
public class ProxyService
{

    @Inject
    DummyJSONClient dummyJSONClient;

    private final Cache<String, CachedResponse> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(15))
            .build();

    public Response getFromOriginUrl(String originUrl, int limit) throws Exception
    {
        System.out.println("IN ProxyService.getFromOriginUrl");
        // append limit as query param
        String sep = originUrl.contains("?") ? "&" : "?";
        String cacheKey = originUrl + sep + "limit=" + limit;

        // Check cache
        CachedResponse cached = cache.getIfPresent(cacheKey);
        if (cached != null) {
            System.out.println("Cache HIT for key: " + cacheKey);
            Response.ResponseBuilder cachedBuilder = Response.status(cached.status()).entity(cached.body());
            cached.headers().forEach((name, values) -> values.forEach(v -> cachedBuilder.header(name, v)));
            cachedBuilder.header("X-Cache", "HIT");
            return cachedBuilder.build();
        }

        System.out.println("Cache MISS for key: " + cacheKey + ". Fetching from origin instead.");
        HttpResponse<String> httpResponse = dummyJSONClient.getFromOriginUrl(cacheKey);

        System.out.println("Received response from origin with status code: " + httpResponse.statusCode());

        // save body to Response
        Response.ResponseBuilder responseBuilder = Response.status(httpResponse.statusCode()).entity(httpResponse.body());

        // Copy headers except pseudo-headers
        Map<String, List<String>> headersMap = httpResponse.headers().map().entrySet().stream()
                .filter(e -> !e.getKey().startsWith(":"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        headersMap.forEach((name, values) -> {
            values.forEach(value -> {
                System.out.println("Adding header to response: " + name + " = " + value);
                responseBuilder.header(name, value);
            });
        });

        // Store in cache (status, body, headers)
        CachedResponse cachedResponse = new CachedResponse(httpResponse.statusCode(), httpResponse.body(), headersMap);
        cache.put(cacheKey, cachedResponse);
        responseBuilder.header("X-Cache", "MISS");

        System.out.println("Returning responseBuilder: " + responseBuilder);

        return responseBuilder.build();
    }

    public void clearCache()
    {
        System.out.println("Attemption to clear cache");
        cache.invalidateAll();
        System.out.println("Cache cleared");
    }
}
