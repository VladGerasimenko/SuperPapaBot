package org.example;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        Application application = new Application();
        CompletableFuture<Void> run = application.run();
        run.join();
    }
}