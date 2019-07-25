/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import com.lzj.arch.core.Contract;

/**
 * 项契约。
 *
 * @author 吴吉林
 */
public interface ItemContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends Contract.PassiveView {

    }

    /**
     * 表现者接口。
     */
    interface Presenter extends Contract.Presenter {

        /**
         * 处理视图项单击事件。
         *
         * @param position 当前视图项位置
         */
        void onItemClick(int position);

        /**
         * 处理视图项长按事件。
         *
         * @param position 当前视图项位置
         */
        void onItemLongClick(int position);

        /**
         * 处理视图项已回收事件。
         */
        void onRecycled();
    }
}
