package org.example.httpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class HttpRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestWrapper.class);
    private final HttpService httpService;

    private final HttpRequestFactory requestFactory;

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
        return () -> {
            try {
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
