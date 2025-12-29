package dev.proxy.service;

import dev.proxy.client.DummyJSONClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.net.http.HttpResponse;

@ApplicationScoped
public class ProxyService
{

    @Inject
    DummyJSONClient dummyJSONClient;

    public Response getFromOriginUrl(String originUrl, int limit) throws Exception
    {
        // append limit as query param
        String modifiedUrl = originUrl;
        String sep = originUrl.contains("?") ? "&" : "?";
        modifiedUrl = originUrl + sep + "limit=" + limit;

        HttpResponse<String> httpResponse = dummyJSONClient.getFromOriginUrl(modifiedUrl);

        // save body to Response
        Response.ResponseBuilder responseBuilder = Response.ok(httpResponse.body());

        // Copy headers, excluding HTTP/2 pseudo-headers
        httpResponse.headers().map().forEach((name, values) -> {
            if (!name.startsWith(":")) {
                values.forEach(value -> responseBuilder.header(name, value));
            }
        });

        return responseBuilder.build();
    }
}
