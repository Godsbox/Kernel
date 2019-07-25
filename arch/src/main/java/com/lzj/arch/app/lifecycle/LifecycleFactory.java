/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

/**
 * 代表生命周期工厂。
 *
 * @author 吴吉林
 */
public interface LifecycleFactory {

    /**
     * 创建屏幕生命周期。
     *
     * @return 屏幕生命周期
     */
    ActivityLifecycle createActivityLifecycle();

    /**
     * 创建界面生命周期。
     *
     * @return 界面生命周期
     */
    FragmentLifecycle createFragmentLifecycle();

    /**
     * 创建表现者生命周期。
     *
     * @return 表现者生命周期
     */
    PresenterLifecycle createPresenterLifecycle();

    /**
     * 创建布局生命周期。
     *
     * @return 布局生命周期
     */
    LayoutLifecycle createLayoutLifecycle();
}
