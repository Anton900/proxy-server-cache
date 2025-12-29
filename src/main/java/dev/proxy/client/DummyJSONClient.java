package dev.proxy.client;

import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class DummyJSONClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getFromOriginUrl(String originUrl) throws Exception {
        System.out.println("In Client");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(originUrl))
                .GET()
                .build();
        System.out.println("Build request to " + originUrl);
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}