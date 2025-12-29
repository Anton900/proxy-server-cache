package dev.proxy.resource;

import dev.proxy.service.ProxyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@Path("/")
public class ProxyResource {

    @ConfigProperty(name = "origin.url")
    Optional<String> originUrl;

    @Inject
    ProxyService proxyService;

    @GET
    public Response getFromOrigin() throws Exception
    {
        System.out.println("In Resource");
        System.out.println("Origin URL: " + originUrl.orElse("not set"));
        if(originUrl.isEmpty())
        {
            return null;
        }
        return proxyService.getFromOriginUrl(originUrl.get());
    }
}