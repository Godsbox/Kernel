/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.util.map;

import java.io.Serializable;

/**
 * 只读映射表。
 *
 * @author 吴吉林
 */
public interface ReadOnlyMap<K> {

    /**
     * 判断是否有给定键的映射。
     *
     * @param key 键
     * @return true：存在；false：不存在。
     */
    boolean contains(K key);

    boolean getBoolean(K key);

    boolean getBoolean(K key, boolean defaultValue);

    String getString(K key);

    String getString(K key, String defaultValue);

    int getInt(K key);

    int getInt(K key, int defaultValue);

    long getLong(K key);

    long getLong(K key, long defaultValue);

    Serializable getSerializable(K key);
}
