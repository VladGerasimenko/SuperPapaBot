package org.example.consts;

public enum Endpoints {
    MAIN_PAGE("https://papajohns.ru/");

    private final String url;

    Endpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
