package com.longbig.multifunction.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author yuyunlong
 * @date 2023/2/18 9:44 下午
 * @description
 */
public class CacheHelper {

    private static Cache<String, String> cache;

    static {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .build();
    }

    public static void set(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }
}
