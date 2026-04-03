package cache;

import model.HttpResponse;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheManager {

    private static final int MAX_CACHE_SIZE = 100;

    private static final Map<String, HttpResponse> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, HttpResponse>(MAX_CACHE_SIZE, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, HttpResponse> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            }
    );

    public static void put(String url, HttpResponse response) {
        if (url != null && response != null && response.getBody() != null) {
            cache.put(url, response);
        }
    }

    public static HttpResponse get(String url) {
        return cache.get(url);
    }

    public static boolean contains(String url) {
        return cache.containsKey(url);
    }

    public static void remove(String url) {
        if (url != null) {
            cache.remove(url);
        }
    }

    public static void clear() {
        cache.clear();
    }
}