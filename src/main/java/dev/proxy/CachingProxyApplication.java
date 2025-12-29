package dev.proxy;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.net.HttpURLConnection;
import java.net.URL;

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

        if (args.length == 3 && args[0].equals("--port") && args[2].equals("--clear-cache")) {
            port = args[1];
            port = System.getProperty("quarkus.http.port", port);
            try {
                URL url = new URL("http://localhost:" + port + "/clear-cache");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Cache cleared successfully on port " + port);
                } else {
                    System.out.println("Failed to clear cache. Response code: " + responseCode);
                }
                conn.disconnect();
            } catch (Exception e) {
                System.err.println("Error clearing cache: " + e.getMessage());
                System.err.println("Make sure the proxy server is running on port " + port);
            }
            return;
        }

        if(port.isEmpty() || origin.isEmpty()) {
            System.err.println("Usage: ./mvnw quarkus:dev -Dquarkus.args='--port <port> --origin <origin_url>'");
            System.err.println("or: ./mvnw quarkus:dev -Dquarkus.args='--port <port> --clear-cache'");
            return;
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
