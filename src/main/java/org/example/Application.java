package org.example;

import org.example.dataCache.IDataCache;
import org.example.dataCache.ProductsCache;
import org.example.dataCache.UserAgentsCache;
import org.example.httpService.HttpRequestFactory;
import org.example.httpService.HttpService;
import org.example.model.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private final AtomicInteger orderId = new AtomicInteger(0);

    private Map<String, String> defaultHeaders = Collections.emptyMap();
    private IDataCache<String> userAgentsCache;
    private IDataCache<Product> productsCache;

    private HttpService httpService;
    public CompletableFuture<Void> run() {
        return CompletableFuture.runAsync(() -> {
            if (defaultHeaders.isEmpty()) {
                UserAgentsCache cache = new UserAgentsCache();
                userAgentsCache = cache.init();
                loadDefaultHeaders();
                httpService = new HttpService(new HttpRequestFactory(defaultHeaders));
            }
        }).thenRun(() -> {
            if (productsCache == null) {
                ProductsCache prodCache = new ProductsCache(httpService);
                productsCache = prodCache.init();
            }
        });
    }

    private void loadDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", userAgentsCache.getRandomValue());
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        this.defaultHeaders = headers;
    }
}
