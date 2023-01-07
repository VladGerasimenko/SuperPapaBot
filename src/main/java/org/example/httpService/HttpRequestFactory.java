package org.example.httpService;

import org.example.consts.HelperEndpoints;
import org.example.consts.PapaEndpoints;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public class HttpRequestFactory {
    private final Map<String, String> defaultHeaders;

    public HttpRequestFactory(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public HttpRequest getMainPageReq() {
        return getRequest(createPapaUri(PapaEndpoints.MAIN_PAGE.getUrl()));
    }

    public HttpRequest getGoodsPageReq() {
        return getRequest(createPapaUri(PapaEndpoints.GOODS_PAGE.getUrl()));
    }

    public HttpRequest getDecryptReq(String bodyAsString) {
        return postRequest(
                createHelperUri(
                        HelperEndpoints.DECRYPT.getUrl(),
                        HelperEndpoints.DECRYPT.getPort(),
                        HelperEndpoints.DECRYPT.getPath()
                ),
                bodyAsString
        );
    }

    public void addDefaultHeader(String headerName, String headerValue) {
        this.defaultHeaders.put(headerName, headerValue);
    }

    private HttpRequest getRequest(URI uri) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .GET();
        setDefaultHeaders(builder);
        return builder.build();
    }

    private HttpRequest postRequest(URI uri, String bodyAsString) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(bodyAsString));
        setDefaultHeaders(builder);
        return builder.build();
    }

    private void setDefaultHeaders(HttpRequest.Builder builder) {
        defaultHeaders.forEach(builder::setHeader);
    }

    private URI createPapaUri(String uri) {
        return URI.create(String.format("%s", uri));
    }

    private URI createHelperUri(String uri, int port, String path) {
        return URI.create(String.format("%s:%d%s", uri, port, path));
    }
}
