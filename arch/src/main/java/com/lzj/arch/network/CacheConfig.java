/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

/**
 * 缓存配置。
 *
 * @author 吴吉林
 */
public class CacheConfig {

    /**
     * 键。
     */
    private String key;

    /**
     * 标识缓存是否开启。
     */
    private boolean cacheEnabled;

    /**
     * 标识是否忽略本地缓存。
     */
    private boolean ignoreCache;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public boolean isIgnoreCache() {
        return ignoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.ignoreCache = ignoreCache;
    }
}
