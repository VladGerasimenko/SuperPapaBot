package org.example.httpService;

import org.example.consts.Endpoints;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public class HttpRequestFactory {
    private final Map<String, String> defaultHeaders;
    public HttpRequestFactory(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public HttpRequest getMainPageReq() {
        return getRequest(Endpoints.MAIN_PAGE.getUrl());
    }

    public HttpRequest getGoodsPageReq() {
        return getRequest(Endpoints.GOODS_PAGE.getUrl());
    }

    private HttpRequest.Builder getDefaultBuilder(String uri) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET();
        setDefaultHeaders(builder);
        return builder;
    }

    private HttpRequest getRequest(String uri) {
        return getDefaultBuilder(uri).build();
    }
    private void setDefaultHeaders(HttpRequest.Builder builder) {
        defaultHeaders.forEach(builder::setHeader);
    }
}
