/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

/**
 * 生命周期管理者，也是生命周期工厂，负责创建应用内重要组件的生命周期。
 *
 * @author 吴吉林
 */
public class LifecycleManager implements LifecycleFactory {

    /**
     * 单例。
     */
    private static final LifecycleManager INSTANCE = new LifecycleManager();

    /**
     * 空屏幕生命周期。
     */
    private static final ActivityLifecycle EMPTY_ACTIVITY_LIFECYCLE = new ActivityLifecycle();

    /**
     * 空界面生命周期。
     */
    private static final FragmentLifecycle EMPTY_FRAGMENT_LIFECYCLE = new FragmentLifecycle();

    /**
     * 空表现者生命周期。
     */
    private static final PresenterLifecycle EMPTY_PRESENTER_LIFECYCLE = new PresenterLifecycle();

    /**
     * 空布局生命周期。
     */
    private static final LayoutLifecycle EMPTY_LAYOUT_LIFECYCLE = new LayoutLifecycle();

    /**
     * 生命周期工厂。
     */
    private LifecycleFactory factory;

    /**
     * 私有构造方法。
     */
    private LifecycleManager() {}

    /**
     * 设置生命周期工厂。
     *
     * @param factory 生命周期工厂
     */
    public void setFactory(LifecycleFactory factory) {
        this.factory = factory;
    }

    @Override
    public ActivityLifecycle createActivityLifecycle() {
        if (factory == null) {
            return EMPTY_ACTIVITY_LIFECYCLE;
        }
        ActivityLifecycle lifecycle = factory.createActivityLifecycle();
        return lifecycle != null ? lifecycle : EMPTY_ACTIVITY_LIFECYCLE;
    }

    @Override
    public FragmentLifecycle createFragmentLifecycle() {
        if (factory == null) {
            return EMPTY_FRAGMENT_LIFECYCLE;
        }
        FragmentLifecycle lifecycle = factory.createFragmentLifecycle();
        return lifecycle != null ? lifecycle : EMPTY_FRAGMENT_LIFECYCLE;
    }

    @Override
    public PresenterLifecycle createPresenterLifecycle() {
        if (factory == null) {
            return EMPTY_PRESENTER_LIFECYCLE;
        }
        PresenterLifecycle lifecycle = factory.createPresenterLifecycle();
        return lifecycle != null ? lifecycle : EMPTY_PRESENTER_LIFECYCLE;
    }

    @Override
    public LayoutLifecycle createLayoutLifecycle() {
        if (factory == null) {
            return EMPTY_LAYOUT_LIFECYCLE;
        }
        LayoutLifecycle lifecycle = factory.createLayoutLifecycle();
        return lifecycle != null ? lifecycle : EMPTY_LAYOUT_LIFECYCLE;
    }

    /**
     * 获取单例。
     *
     * @return 单例
     */
    public static LifecycleManager getInstance() {
        return INSTANCE;
    }
}
