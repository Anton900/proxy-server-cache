package dev.proxy.model.cache;

import java.util.List;
import java.util.Map;

public record CachedResponse(int status, String body, Map<String, List<String>> headers)
{
}

