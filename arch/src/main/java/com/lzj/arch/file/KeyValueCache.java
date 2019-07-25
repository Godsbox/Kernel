/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.file;

import com.lzj.arch.util.map.ReadOnlyMap;

/**
 * 键值对缓存。请使用SPUtils代替，简化开发
 */
@Deprecated
public interface KeyValueCache extends ReadOnlyMap<String> {

    KeyValueCache remove(String key);

    String getName();

    KeyValueCache putString(String key, String value);

    KeyValueCache putBoolean(String key, boolean value);

    KeyValueCache putInt(String key, int value);

    KeyValueCache putLong(String key, long value);

    void apply();
}
