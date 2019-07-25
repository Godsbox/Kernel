/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import com.lzj.arch.core.Model;
import com.lzj.arch.util.StringUtils;

/**
 * 内容表现模型。
 *
 * @author 吴吉林
 */
public abstract class ContentModel<T> extends Model {

    /**
     * 请求类型：加载。
     */
    public static final int TYPE_LOAD    = 1;

    /**
     * 请求类型：刷新。
     */
    public static final int TYPE_REFRESH = 2;

    /**
     * 请求类型：加载更多。
     */
    public static final int TYPE_LOAD_MORE = 3;

    /**
     * 请求类型：刷新更多。
     */
    public static final int TYPE_REFRESH_MORE = 4;

    /**
     * 标识是否延迟加载内容。
     */
    private boolean lazyLoad = true;

    /**
     * 标识是否显示加载中状态。
     */
    private boolean showLoading = true;

    /**
     */
    private boolean showRefreshingOnLoading;

    /**
     * 标识是否启用刷新功能。
     */
    private boolean refreshEnabled = true;

    /**
     * 标识是否启用让内容新鲜功能。
     */
    private boolean freshContentEnabled;

    /**
     * 错误码。
     */
    private int errorCode;

    /**
     * 错误消息。
     */
    private CharSequence errorMessage;

    /**
     * 内容请求类型。
     */
    private int type;

    /**
     * 加载后是否滑动到顶部
     */
    private boolean loadContentedScrollTop = true;

    private T content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isLoadContentedScrollTop() {
        return loadContentedScrollTop;
    }

    public void setLoadContentedScrollTop(boolean loadContentedScrollTop) {
        this.loadContentedScrollTop = loadContentedScrollTop;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public CharSequence getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setError(int errorCode, CharSequence errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        if(errorMessage == null || StringUtils.isEmpty(errorMessage.toString())){
            return false;
        }
        return true;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean show) {
        this.showLoading = show;
    }

    public boolean isLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

    public boolean isFreshContentEnabled() {
        return freshContentEnabled;
    }

    public void setFreshContentEnabled(boolean enabled) {
        this.freshContentEnabled = enabled;
    }

    /**
     * 判断是否无内容。
     *
     * @return true：无内容；false：有内容。
     */
    public boolean isEmpty() {
        return isContentEmpty(getContent());
    }

    /**
     * 判断给定的内容是否是无内容。
     *
     * @param content 内容
     * @return true：无内容；false：有内容。
     */
    public boolean isContentEmpty(T content) {
        return content == null;
    }

    public boolean isShowRefreshingOnLoading() {
        return showRefreshingOnLoading;
    }

    public void setShowRefreshingOnLoading(boolean showRefreshingOnLoading) {
        this.showRefreshingOnLoading = showRefreshingOnLoading;
    }
}
