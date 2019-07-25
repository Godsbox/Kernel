/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app;

import android.os.Bundle;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;

import com.lzj.arch.app.PassiveContract.PassiveView;
import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.core.Controller;

/**
 * @author 吴吉林
 */
interface PassiveController<P extends Presenter>
        extends Controller<P>, PassiveView, OnMenuItemClickListener {

    /**
     * 处理视图查找。
     */
    void onFindView();

    /**
     * 查找视图元素。该方法根据泛型机制的类型推导，省去手写强制转换。
     *
     * @param id 视图 ID
     * @param <V> 视图类型
     * @return 视图
     */
    <V> V findView(int id);

    /**
     * 处理视图初始化。
     *
     * @param state 视图状态
     */
    void onInitView(Bundle state);

    /**
     * 处理导航图标单击事件。
     */
    void onNavigationClick();
}
