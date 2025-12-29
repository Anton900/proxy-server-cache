package dev.proxy.resource;

import dev.proxy.service.ProxyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
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
    public Response getFromOrigin(@QueryParam("limit") @DefaultValue("10") int limit) throws Exception
    {
        System.out.println("Origin URL: " + originUrl.orElse("not set") + " | limit: " + limit);
        if(originUrl.isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Origin URL is not configured")
                    .build();
        }
        return proxyService.getFromOriginUrl(originUrl.get(), limit);
    }
}