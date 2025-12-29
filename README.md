# proxy-server-cache

A proxy server that forwards requests to a configured origin URL (set via the origin.url config property or the --origin command-line parameter). Requests may include a "limit" query parameter (defaults to 10) which will be forwarded to the origin.

The application caches proxied responses in-memory using Caffeine; cached entries expire after 15 minutes.

This project idea and instructions are from https://roadmap.sh/projects/caching-server.

## Tech stack
- Java 21
- Quarkus
- Caffeine (in-memory cache)
- Maven

## How to start

### Quarkus dev mode
  - ./mvnw quarkus:dev -Dquarkus.args="--port `<number>` --origin `<URL>`"
  - ./mvnw quarkus:dev -Dquarkus.args="--port 3000 --origin https://dummyjson.com/products"

### Packaged application (example)
  - mvn package
  - java -jar target/*-runner.jar --port 3000 --origin http://dummyjson.com/products

### Clear cache
  - To clear the cache on startup, make sure there's an existing Quarkus application running on that port already. 
  - Then start a new application with the --clear-cache argument:
  - java -jar target/*-runner.jar --port 3000 --clear-cache
  - or run with args: ./mvnw quarkus:dev -Dquarkus.args="--port 3000 --clear-cache"

### After app start
- Open browser or use curl to http://localhost:3000/.

#### Examples
- http://localhost:3000/?limit=5 (in browser)
- curl http://localhost:3000/products?limit=15
