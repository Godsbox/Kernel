/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import com.lzj.arch.app.PassiveContract;

/**
 * 内容契约。
 *
 * @author 吴吉林
 */
public interface ContentContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends PassiveContract.PassiveView {

        /**
         * 显示内容加载中。
         */
        void showLoading();

        /**
         * 隐藏内容加载中。
         */
        void hideLoading();

        /**
         * 显示内容加载失败状态。
         *
         * @param errorMessage 错误消息
         */
        void showLoadFailure(CharSequence errorMessage);

        /**
         * 显示无加载内容状态。
         */
        void showLoadEmpty();

        /**
         * 显示副无加载内容状态。
         *
         * @param title 标题
         */
        void showLoadEmptySecondary(CharSequence title);

        /**
         * 设置刷新功能是否启用。
         *
         * @param enabled true：启用；false：禁用。
         */
        void setRefreshEnabled(boolean enabled);

        /**
         * 设置刷新状态。<br /><br />
         *
         * 即设置是否展示刷新视图。
         *
         * @param refreshing true：刷新中；false：已刷新。
         */
        void setRefreshing(boolean refreshing);

        /**
         * 显示刷新失败错误消息。
         *
         * @param message 错误消息
         */
        void showRefreshFailure(String message);

        /**
         * 显示刷新无内容状态。
         */
        void showRefreshEmpty();
    }

    /**
     * 表现者接口。
     */
    interface Presenter extends PassiveContract.Presenter {
        /**
         * 处理无内容单击事件。
         */
        void onEmptyClick();

        /**
         * 重新加载内容。
         */
        void onReloadClick();

        /**
         * 处理刷新触发事件。
         */
        void onRefreshTrigger();

        /**
         * 自定义事件
         */
        void onDiyClick();

        /**
         * 获取登陆状态
         * @return
         */
        boolean isLogin();
    }
}
