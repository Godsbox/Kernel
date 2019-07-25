/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.layout;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lzj.arch.app.lifecycle.LayoutLifecycle;
import com.lzj.arch.app.lifecycle.LifecycleManager;
import com.lzj.arch.core.Contract;
import com.lzj.arch.core.Controller;
import com.lzj.arch.core.PresenterDelegate;
import com.lzj.arch.core.PresenterManager;

import static com.lzj.arch.core.Arch.newPresenterDelegate;
import static com.lzj.arch.util.ContextUtils.toActivity;

/**
 * 被动线性布局。
 *
 * @author 吴吉林
 */
public abstract class PassiveLinearLayout<P extends Contract.Presenter>
        extends LinearLayout
        implements Controller<P>, Layout {

    /**
     * 生命周期。
     */
    private LayoutLifecycle lifecycle;

    /**
     * 表现者委托。
     */
    private PresenterDelegate<P> presenterDelegate = newPresenterDelegate(this);

    public PassiveLinearLayout(Context context) {
        this(context, null, 0);
    }

    public PassiveLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassiveLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        presenterDelegate.onViewCreate(this, null, null, PresenterManager.getDefault());
        getLifecycle().onCreate(toActivity(context));
    }

    protected LayoutLifecycle getLifecycle() {
        if (lifecycle == null) {
            lifecycle = LifecycleManager.getInstance().createLayoutLifecycle();
        }
        return lifecycle;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onFindView();
        onInitView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenterDelegate.attachView(this, true);
        presenterDelegate.onViewResume(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenterDelegate.onViewPause();
        presenterDelegate.detachView();
        presenterDelegate.onLayoutDestroy(toActivity(getContext()));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        Bundle state = new Bundle();
        presenterDelegate.onSaveState(state);
        return state;
    }

    @Override
    public P createPresenter() {
        return presenterDelegate.createPresenter();
    }

    @Override
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    public Contract.Router getRouter() {
        return getLifecycle().getRouter();
    }

    @Override
    public void onFindView() {
        // 空实现
    }

    @Override
    public void onInitView() {
        // 空实现
    }
}
