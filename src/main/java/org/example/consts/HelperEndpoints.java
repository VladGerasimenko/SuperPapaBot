package org.example.consts;

public enum HelperEndpoints {
    DECRYPT("http://127.0.0.1", 8081, "/decrypt");
    private final String url;
    private final int port;
    private final String path;

    HelperEndpoints(String url, int port, String path) {
        this.url = url;
        this.port = port;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
