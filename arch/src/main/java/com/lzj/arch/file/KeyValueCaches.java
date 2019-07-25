/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.file;

import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * 请使用SPUtils代替，简化开发(单条数据请使用SP)
 */
@Deprecated
public final class KeyValueCaches {

    private static final Map<String, KeyValueCache> CACHE_MAP = new ArrayMap<>();

    public static void add(KeyValueCache cache) {
        if (cache == null) {
            return;
        }
        CACHE_MAP.put(cache.getName(), cache);
    }

    public static KeyValueCache get(String name) {
        return CACHE_MAP.get(name);
    }
}
