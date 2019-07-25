/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.group;

import com.lzj.arch.app.content.ContentContract;

/**
 * 分组内容契约。
 *
 * @author 吴吉林
 */
public interface GroupContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ContentContract.PassiveView, BasicPassiveView {

        /**
         * 页面数据发生变化
         */
        void notifyPagerChange();

        /**
         * 获取当前位置
         * @return
         */
        int getCurrentItem();

    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ContentContract.Presenter {

        /**
         * 处理页面变化事件。
         *
         * @param position 当前页面位置
         */
        void onPageChange(int position);

        /**
         * 处理Tab重复选中事件。
         *
         * @param position 当前页面位置
         */
        void onTabReselected(int position);
    }

    /**
     * 基础视图接口。
     */
    interface BasicPassiveView {

        /**
         * 显示给定位置的界面。
         *
         * @param position 界面
         */
        void showFragment(int position);
    }
}
