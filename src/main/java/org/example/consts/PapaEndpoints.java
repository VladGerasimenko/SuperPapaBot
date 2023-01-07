package org.example.consts;

public enum PapaEndpoints {
    MAIN_PAGE("https://papajohns.ru/moscow"),
    GOODS_PAGE("https://api.papajohns.ru/catalog/goods?city_id=1");

    private final String url;

    PapaEndpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
