package org.example.httpService;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class HttpService {
    private final HttpClient httpClient;

    private final HttpRequestFactory requestFactory;

    private final HttpRequestWrapper requestWrapper;

    public HttpService(HttpRequestFactory requestFactory) {
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        this.requestFactory = requestFactory;
        this.requestWrapper = new HttpRequestWrapper(this, requestFactory);
    }
    public HttpResponse getMainPage() {
        return requestWrapper.executeWrapped(requestFactory.getMainPageReq(), 200);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}

