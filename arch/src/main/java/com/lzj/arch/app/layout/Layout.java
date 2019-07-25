/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.layout;

/**
 * 布局。
 *
 * @author 吴吉林
 */
public interface Layout {

    /**
     * 在查找视图项子视图时调用。
     */
    void onFindView();

    /**
     * 在初始化视图项子视图调用。
     */
    void onInitView();
}
