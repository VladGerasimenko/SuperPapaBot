package org.example.httpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestWrapper.class);
    private final HttpService httpService;

    private final HttpRequestFactory requestFactory;

    private boolean isAuthorized = false;

    public HttpRequestWrapper(HttpService httpService, HttpRequestFactory requestFactory) {
        this.httpService = httpService;
        this.requestFactory = requestFactory;
    }

    public HttpResponse executeWrapped(HttpRequest request, int expectedStatus) {
        HttpResponse response = wrap(request).get();
        if (response.statusCode() == expectedStatus) {
            logger.info("Request was successful");
        } else {
            logger.error("Invalid status code received: ${}", response.statusCode());
        }
        return response;
    }

    private Supplier<HttpResponse> wrap(HttpRequest request) {
        Pattern extractPrivateKey = Pattern.compile("setPrivateKey\\(\"(.+)\"\\)");
        Pattern extractMessage = Pattern.compile("decrypt\\(\"(.+)\"\\)");
        return () -> {
            try {
                if (!isAuthorized) {
                    logger.info("Sending initial request for settng cookies");
                    HttpResponse<String> send = httpService.getHttpClient().send(requestFactory.getGoodsPageReq(), HttpResponse.BodyHandlers.ofString());
                    Matcher matcher = extractPrivateKey.matcher(send.body());
                    Matcher matcher1 = extractMessage.matcher(send.body());
                    if (matcher.find()) {
                        String group = matcher.group(1);
                    }
                    if (matcher1.find()) {
                        String group = matcher1.group(1);
                    }
                    isAuthorized = true;
                }
                logger.info(createLogMessage(request));
                return httpService.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Throwable t) {
                logger.error(t.getMessage());
                return null;
            }
        };
    }

    private String createLogMessage(HttpRequest httpRequest) {
        return "Sending the following request: " +
                "Url: " +
                httpRequest.uri() +
                "Method: " +
                httpRequest.method() +
                "Headers" +
                httpRequest.headers().map();
    }
}
