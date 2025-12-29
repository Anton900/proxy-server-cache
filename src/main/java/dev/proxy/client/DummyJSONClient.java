package dev.proxy.client;

import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class DummyJSONClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public HttpResponse<String> getFromOriginUrl(String originUrl) throws Exception {
        System.out.println("IN DummyJSONClient.getFromOriginUrl: " + originUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(originUrl))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}