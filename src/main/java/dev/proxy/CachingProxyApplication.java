package dev.proxy;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CachingProxyApplication implements QuarkusApplication
{
    public static void main(String... args) {
        String port = "";
        String origin = "";

        if (args.length == 4 && args[0].equals("--port") && args[2].equals("--origin")) {
            port = args[1];
            origin = args[3];
            System.setProperty("quarkus.http.port", port);
            System.setProperty("origin.url", origin);
        }

        if (args.length == 1 && args[0].equals("--clear-cache")) {
            port = "3000";
            origin = "clear-cache";
            System.setProperty("quarkus.http.port", port);
            System.setProperty("origin.url", origin);
        }

        System.out.println("Starting proxy on port " + port + " with origin " + origin);
        Quarkus.run(CachingProxyApplication.class, args);
    }

    @Override
    public int run(String... args)
    {
        Quarkus.waitForExit();
        return 0;
    }
}
