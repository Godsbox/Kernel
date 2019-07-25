/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.web;

import android.app.Activity;

import com.lzj.arch.app.content.ContentContract;

/**
 * 网页内容契约。
 *
 * @author 吴吉林
 */
public interface WebContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ContentContract.PassiveView {

        /**
         * 加载网页。
         *
         * @param url 网页 URL 地址
         */
        void load(String url);

        /**
         * 刷新网页。
         */
        void refresh();

        /**
         * 回调前端接口。
         *
         * @param callback 接口名称
         */
        void callback(String callback);

        /**
         * 回调前端接口。
         *
         * @param callback 接口名称
         * @param param    参数 如果类型为String时必须是Json格式 H5那里才能解析
         */
        void callback(String callback, Object param);

        /**
         * 设置加载进度条显隐藏。
         *
         * @param visible true：显示；false：隐藏。
         */
        void setProgressVisible(boolean visible);

        Activity getActivity();
    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ContentContract.Presenter {

        /**
         * 处理网页加载开始事件。
         *
         * @param url 网页 URL 地址
         */
        void onPageStart(String url);

        /**
         * 处理网页加载完成事件。<br /><br />
         * <p>
         * 该方法在网页成功或失败时都会调用。失败时，该方法在 {@link #onPageError(int, String, String)} 之后
         * 调用。
         *
         * @param url 网页 URL 地址
         */
        void onPageEnd(String url);

        /**
         * 处理网页加载失败事件。
         *
         * @param errorCode 错误码
         * @param message   错误消息
         * @param url       网页 URL 地址
         */
        void onPageError(int errorCode, String message, String url);

        /**
         * 处理标题已接收事件。
         *
         * @param title 网页标题
         */
        void onTitleReceived(String title);
    }
}
