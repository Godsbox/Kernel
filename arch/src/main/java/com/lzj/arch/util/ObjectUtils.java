/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzj.arch.network.ApiClient;

/**
 * 关于对象操作方法的工具类。
 *
 * @author wsy
 */
public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

    /**
     * 将Object对象转换成JSON字符串
     */
    public static <T> String objectToJsonStr(T obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 写入给定 URL 地址的硬盘缓存。
     *
     * @param key 键
     * @param json JSON 数据
     */
    public static <T> void writeObjectDiskCache(String key, T json) {
        ApiClient.writeDiskCache(key,objectToJsonStr(json));
    }

    /**
     * 读取给定 URL 地址的硬盘缓存。
     *
     * @param key 键
     * @return URL 地址的硬盘缓存，若无缓存则返回null。
     */
    public static JsonObject readObjectDiskCache(String key) {
        //ApiClient.readDiskCache()
        return null;
    }
}
