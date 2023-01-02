package org.example.dataCache;

import org.example.dataCache.IDataCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserAgentsCache implements IDataCache<String> {
    private final List<String> cache = new ArrayList<>();

    @Override
    public IDataCache init() {
        try {
            File userAgentsFile = new File(new File(".").getCanonicalPath() + "/src/main/resources/user-agents.txt");
            try (BufferedReader br = new BufferedReader(new FileReader(userAgentsFile))) {
                while (br.readLine() != null) {
                    cache.add(br.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    @Override
    public String getRandomValue() {
        return cache.get(IDataCache.getRandomIdx(0, cache.size()));
    }

    @Override
    public List<String> getCache() {
        return cache;
    }
}
