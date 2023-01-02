package org.example.dataCache;

import org.example.httpService.HttpService;
import org.example.model.Product;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductsCache implements IDataCache<Product> {
    private final Map<String, Product> productsCache = Collections.emptyMap();
    private final HttpService httpService;
    public ProductsCache(HttpService httpService) {
        this.httpService = httpService;
    }
    @Override
    public IDataCache<Product> init() {
        HttpResponse mainPage = httpService.getMainPage();
        return null;
    }

    @Override
    public Product getRandomValue() {
        List<Product> collect = new ArrayList<>(productsCache.values());
        return collect.get(IDataCache.getRandomIdx(0, productsCache.size()));
    }

    @Override
    public List<Product> getCache() {
        return new ArrayList<>(productsCache.values());
    }
}
