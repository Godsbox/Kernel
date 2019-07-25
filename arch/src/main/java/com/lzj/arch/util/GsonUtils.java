/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * 关于 GSON 操作方法的工具类。
 *
 * @author 吴吉林
 */
public final class GsonUtils {

    private GsonUtils() {
    }

    public static boolean has(JsonObject object, String key) {
        return object != null && object.has(key);
    }

    public static String getString(JsonObject object, String key) {
        return getString(object, key, "");
    }

    public static String getString(JsonObject object, String key, String defValue) {
        if (object == null) {
            return defValue;
        }
        if (!object.has(key)) {
            return defValue;
        }
        try {
            JsonElement element = object.getAsJsonPrimitive(key);
            return element.getAsString();
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public static long getLong(JsonObject object, String key) {
        return getLong(object, key, 0L);
    }

    public static long getLong(JsonObject object, String key, long defValue) {
        if (object == null) {
            return defValue;
        }
        if (!object.has(key)) {
            return defValue;
        }
        try {
            JsonElement element = object.getAsJsonPrimitive(key);
            return element.getAsLong();
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public static int getInt(JsonObject object, String key) {
        return getInt(object, key, 0);
    }

    public static int getInt(JsonObject object, String key, int defValue) {
        if (object == null) {
            return defValue;
        }
        if (!object.has(key)) {
            return defValue;
        }
        try {
            JsonElement element = object.getAsJsonPrimitive(key);
            return element.getAsInt();
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public static boolean getBoolean(JsonObject object, String key) {
        return getBoolean(object, key, false);
    }

    public static boolean getBoolean(JsonObject object, String key, boolean defValue) {
        if (object == null) {
            return defValue;
        }
        if (!object.has(key)) {
            return defValue;
        }
        try {
            JsonElement element = object.getAsJsonPrimitive(key);
            return element.getAsBoolean();
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public static JsonObject getJsonObject(JsonObject object, String key) {
        if (object == null) {
            return new JsonObject();
        }
        if (!object.has(key)) {
            return new JsonObject();
        }
        try {
            return object.getAsJsonObject(key);
        } catch (ClassCastException e) {
            return new JsonObject();
        }
    }

    public static JsonObject getJsonObject(JsonArray array, int index) {
        if (array == null) {
            return new JsonObject();
        }
        if (array.size() == 0) {
            return new JsonObject();
        }
        try {
            return array.get(index).getAsJsonObject();
        } catch (RuntimeException e) {
            return new JsonObject();
        }
    }

    public static JsonArray getJsonArray(JsonObject object, String key) {
        if (object == null) {
            return new JsonArray();
        }
        if (!object.has(key)) {
            return new JsonArray();
        }
        try {
            return object.getAsJsonArray(key);
        } catch (ClassCastException e) {
            return new JsonArray();
        }
    }

    /**
     * 判断一个 JSON 对象是否空集。
     *
     * @param object JSON 对象
     * @return true：空集；false：不是空集
     */
    public static boolean isEmpty(JsonObject object) {
        return object == null || object.entrySet().size() == 0;
    }

    /**
     * 判断给定的 JSON 对象是否包含某成员。
     *
     * @param object JSON 对象
     * @param key    成员名字
     * @return true：包含；false：不包含。
     */
    public static boolean contains(JsonObject object, String key) {
        return object != null && object.has(key);
    }

    /**
     * json转对象
     */
    public static <T> T json2object(Class<T> cls, String json) {
        if (android.text.TextUtils.isEmpty(json)) {
            return null;
        }
        T t = null;
        try {
            t = new Gson().fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 对象转json
     */
    public static String object2json(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * json转数组
     */
    public static <T> List<T> json2List(Class<T[]> clazz, String json) {
        if (android.text.TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }
}
