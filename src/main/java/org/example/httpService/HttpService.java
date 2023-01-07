package org.example.httpService;

import org.example.utils.HttpUtils;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class HttpService {
    private final HttpClient httpClient;

    private final HttpRequestFactory requestFactory;

    private final HttpRequestWrapper requestWrapper;

    public HttpService(HttpRequestFactory requestFactory) {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.requestFactory = requestFactory;
        this.requestWrapper = new HttpRequestWrapper(this, requestFactory);
    }
    public HttpResponse getMainPage() {
        return requestWrapper.executeWrapped(requestFactory.getMainPageReq(), 200);
    }

    public HttpResponse getGoodsPage() {
        return requestWrapper.executeWrapped(requestFactory.getGoodsPageReq(), 200);
    }

    private HttpResponse decrypt(String bodyAsString) {
        return requestWrapper.executeWrapped(requestFactory.getDecryptReq(bodyAsString), 200);
    }

    public String extractToken() {
        HttpResponse goodsPageRes = getGoodsPage();
        String[] decryptionData = HttpUtils.extractDecryptionData(goodsPageRes.body().toString());
        Map<String, String> decryptReqBody = new HashMap<>() {{
            put("privateKey", decryptionData[0]);
            put("message", decryptionData[1]);
            put("salt", decryptionData[2]);
        }};
        return decrypt(HttpUtils.parseMapToJson(decryptReqBody)).body().toString();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setCookies(String... cookies) {
        StringBuilder result = new StringBuilder();
        for (String cookie: cookies) {
            result.append(cookie).append("; ");
        }
        requestFactory.addDefaultHeader("Cookie", result.substring(0, result.length() - 2));
    }
}

