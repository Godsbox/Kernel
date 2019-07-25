/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.web;

import android.webkit.JavascriptInterface;

import com.lzj.arch.app.web.WebContract.Presenter;

/**
 * Web 接口。
 *
 * @author 吴吉林
 */
public abstract class WebInterface<P extends Presenter> {

    /**
     * 当前界面。
     */
    private WebFragment<P> fragment;

    /**
     * 接口名称。
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFragment(WebFragment<P> fragment) {
        this.fragment = fragment;
    }

    /**
     * 获取当前界面的表现者。
     *
     * @return 表现者
     */
    public P getPresenter() {
        return fragment.getPresenter();
    }

    @JavascriptInterface
    public void foo() {
    }
}
