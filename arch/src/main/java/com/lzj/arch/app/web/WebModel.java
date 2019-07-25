/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.web;

import com.lzj.arch.app.content.ContentModel;

/**
 * 网页内容表现模型。
 *
 * @author 吴吉林
 */
public class WebModel extends ContentModel<Object> {

    /**
     * 标识是否接收到错误。
     */
    private boolean errorReceived;

    /**
     * 获取 URL 地址。
     *
     * @return URL 地址
     */
    public String getUrl() {
        return getParams().getString(WebConstant.EXTRA_URL);
    }

    /**
     *
     * @deprecated 即将删除。
     * @param url
     */
    @Deprecated
    public void setUrl(String url) {

    }

    boolean isErrorReceived() {
        return errorReceived;
    }

    void setErrorReceived(boolean errorReceived) {
        this.errorReceived = errorReceived;
    }

}
