package org.example.dataCache;

import java.util.List;

public interface IDataCache<T> {
    IDataCache<T> init();

    T getRandomValue();

    List<T> getCache();

    static int getRandomIdx(int min, int max) {
        return  min + (int)(Math.random() * max);
    }
}
