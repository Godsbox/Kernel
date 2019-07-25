/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.collection;

import com.google.gson.annotations.SerializedName;

/**
 * 集合内容查询结果。
 *
 * @author 吴吉林
 */
public abstract class CollectionResult {

    /**
     * 是否还能加载更多。
     */
    @SerializedName("more")
    private boolean hasMore;

    /**
     * 是否来自本地缓存。
     */
    private boolean fromCache;

    /**
     * 判断是否还能加载更多。
     *
     * @return true：还有更多；false：没有更多。
     */
    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 设置是否来自本地缓存。
     *
     * @param fromCache true：缓存；false：网络。
     */
    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    /**
     * 判断是否来自缓存。
     *
     * @return true：缓存；false：网络。
     */
    public boolean isFromCache() {
        return fromCache;
    }
}
