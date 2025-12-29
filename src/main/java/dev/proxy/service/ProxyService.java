package dev.proxy.service;

import dev.proxy.client.DummyJSONClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ProxyService
{

    @Inject
    DummyJSONClient dummyJSONClient;

    public Response getFromOriginUrl(String originUrl) throws Exception
    {
        System.out.println("In Service");
        String responseBody = dummyJSONClient.getFromOriginUrl(originUrl);
        System.out.println("Response Body: " + responseBody);
        return Response.ok(responseBody).build();
    }
}
