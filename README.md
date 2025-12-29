# proxy-server-cache

A proxy server that forwards requests to a configured origin URL (set via the origin.url config property or the --origin command-line parameter). Requests may include a "limit" query parameter (defaults to 10) which will be forwarded to the origin.
The application caches proxied responses in-memory using Caffeine; cached entries expire after 15 minutes.

This project idea and instructions are from https://roadmap.sh/projects/caching-server.

Tech stack
- Java 21
- Quarkus
- Caffeine (in-memory cache)
- Maven

How to start

- Quarkus dev mode (live reload)
  - mvn quarkus:dev -Dquarkus.http.port=3000 -Dorigin.url=http://dummyjson.com

- Packaged application (example)
  - mvn package
  - java -jar target/*-runner.jar --port 3000 --origin http://dummyjson.com

- Clear cache (special run mode)
  - java -jar target/*-runner.jar --clear-cache
  - or run with args: --port 3000 --origin clear-cache (the app recognizes --clear-cache)

Examples
- GET /products?limit=5  -> proxied to {origin}/products?limit=5 (limit defaults to 10 if omitted)
- GET /users           -> proxied to {origin}/users?limit=10
