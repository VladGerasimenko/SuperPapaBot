package org.example;

import org.example.dataCache.IDataCache;
import org.example.dataCache.UserAgentsCache;
import org.example.httpService.HttpRequestFactory;
import org.example.httpService.HttpService;
import org.example.model.Product;
import org.example.utils.HttpUtils;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Application {
    private Map<String, String> defaultHeaders = Collections.emptyMap();
    private IDataCache<String> userAgentsCache;
    private IDataCache<Product> productsCache;
    private HttpService httpService;

    private String token;
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
                String json = httpService.extractToken();
                String ippKey = HttpUtils.getValueByKeyFromJson(json, "ippKey");
                String ippUid = HttpUtils.getValueByKeyFromJson(json, "ippUid");
                String ippSign = HttpUtils.getValueByKeyFromJson(json, "ippSign");
                httpService.setCookies(ippKey, ippUid, ippSign, "locale=ru", "city_id=1");
                HttpResponse goodsPage = httpService.getGoodsPage();
                System.out.println(goodsPage.body().toString());
            }
        });
    }
    private void loadDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", userAgentsCache.getRandomValue());
        this.defaultHeaders = headers;
    }
}
