/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app;

import android.support.annotation.CallSuper;

import com.lzj.arch.app.lifecycle.LifecycleManager;
import com.lzj.arch.app.lifecycle.PresenterLifecycle;
import com.lzj.arch.bus.BaseMessageEvent;
import com.lzj.arch.bus.DownloadEvent;
import com.lzj.arch.core.AbstractPresenter;
import com.lzj.arch.core.Contract;
import com.lzj.arch.core.Contract.PassiveView;
import com.lzj.arch.core.Model;
import com.lzj.arch.util.map.StringMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * 被动视图的表现者基类。
 *
 * @author 吴吉林
 */
public abstract class PassivePresenter
        <V extends PassiveView, M extends Model, R extends Contract.Router>
        extends AbstractPresenter<V, M, R>
        implements PassiveContract.Presenter {
    /**
     * 日志用的TAG
     */
    protected final String TAG = getClass().getSimpleName();
    /**
     * 表现者生命周期。
     */
    private PresenterLifecycle lifecycle;

    /**
     * 管理订阅者
     */
    private CompositeDisposable mDisposables;

    @CallSuper
    @Override
    protected void onCreate() {
        super.onCreate();
        getLifecycle().onCreate(this);
    }

    /**
     * 添加订阅者
     * @param observer
     */
    public void addObserver(DisposableObserver observer){
        if(mDisposables == null){
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(observer);
    }

    /**
     * 设置表现者生命周期。
     *
     * @param lifecycle 表现者生命周期
     */
    protected void setLifecycle(PresenterLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    /**
     * 获取表现者生命周期。
     *
     * @return 表现者生命周期
     */
    private PresenterLifecycle getLifecycle() {
        if (lifecycle == null) {
            lifecycle = LifecycleManager.getInstance().createPresenterLifecycle();
        }
        return lifecycle;
    }

    @CallSuper
    @Override
    protected void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        super.onViewAttach(reattach, newView, isVisibleToUser);
        getLifecycle().onViewAttach(this, reattach);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        getLifecycle().onResume(this);
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
        getLifecycle().onPause(this);
    }

    @CallSuper
    @Override
    protected void onViewDetach() {
        super.onViewDetach();
        getLifecycle().onViewDetach(this);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposables != null){
            mDisposables.clear();
        }
        getLifecycle().onDestroy(this);
    }

    @Override
    public void onOkResult(int requestCode, StringMap data) {
        // 空实现
    }

    @Override
    public void onCanceledResult(int requestCode) {
        // 空实现
    }

    /**
     * 订阅并处理空事件。
     *
     * @param event 空事件
     */
    public void onEvent(Void event) {
        // 空实现
    }

    /**
     * 订阅并处理空事件。
     *
     * @param event 空事件
     */
    public void onEvent(BaseMessageEvent event) {
        // 空实现
    }

    /**
     * 订阅并处理空事件。
     *
     * @param event 空事件
     */
    public void onEvent(DownloadEvent event) {
        // 空实现
    }
}
