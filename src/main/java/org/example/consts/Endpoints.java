package org.example.consts;

public enum Endpoints {
    MAIN_PAGE("https://papajohns.ru/"),
    GOODS_PAGE("https://api.papajohns.ru/catalog/goods?city_id=1");

    private final String url;

    Endpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
