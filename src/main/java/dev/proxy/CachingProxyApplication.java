package dev.proxy;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@QuarkusMain
public class CachingProxyApplication implements QuarkusApplication
{
    @ConfigProperty(name = "quarkus.http.port")
    String port;

    @ConfigProperty(name = "origin.url")
    Optional<String> originUrl;

    public static void main(String... args) {
        if (args.length == 4 && args[0].equals("--port") && args[2].equals("--origin")) {
            String port = args[1];
            String origin = args[3];
            System.setProperty("quarkus.http.port", port);
            System.setProperty("origin.url", origin);
            System.out.println("Starting proxy on port " + port + " with origin " + origin);
        }

        Quarkus.run(CachingProxyApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        if (!port.isEmpty() && originUrl.isPresent()) {
            System.out.println("Starting proxy on port " + port + " with origin " + originUrl.get());
            Quarkus.waitForExit();
            return 0;
        }

        if (args.length == 2 && args[0].equals("--clear-cache")) {
            System.out.println("Clearing cache...");
            return 0;
        }

        System.out.println("Invalid arguments. Use --port <number> --origin <url> or --clear-cache");
        return 1;
    }
}
