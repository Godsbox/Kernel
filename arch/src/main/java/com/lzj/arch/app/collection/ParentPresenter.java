/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.annotation.ColorInt;

import com.lzj.arch.app.collection.CollectionContract.PassiveView;

/**
 * @author 吴吉林
 */
public interface ParentPresenter {

    /**
     * 获取视图。
     *
     * @return 视图
     */
    PassiveView getView();

    /**
     * 设置应用栏颜色。
     *
     * @param color 应用栏颜色
     */
    void setAppbarColor(@ColorInt int color);

    /**
     * 设置应用栏显示/隐藏。
     */
    void setAppbarVisible(int visible);

    /**
     * 设置当前项表现者。
     *
     * @param clean 是否清除当前项表现者
     * @param presenter 当前项表现者
     */
    void setCurrentPresenter(boolean clean, ItemPresenter presenter);
}
