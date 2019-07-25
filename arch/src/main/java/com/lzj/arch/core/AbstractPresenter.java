/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import com.lzj.arch.core.Contract.PassiveView;
import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.util.map.StringMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 表现者基类。<br /><br />
 * <p>
 * 该类提供了 {@link Presenter} 接口的抽象实现。<br /><br />
 *
 * @param <M> 表现模型类型，如果没有表现模型，则传 {@link Model}。
 * @author 吴吉林
 */
@SuppressWarnings("unchecked")
public abstract class AbstractPresenter
        <V extends PassiveView, M extends Model, R extends Router>
        implements Presenter {

    /**
     * 视图代理。
     */
    private TargetProxy<V> viewProxy = new TargetProxy<>();

    /**
     * 路由器代理。
     */
    private TargetProxy<R> routerProxy = new TargetProxy<>();

    /**
     * 表现模型类型。
     */
    private Class<M> modelClass;

    /**
     * 表现模型。
     */
    private M model;

    /**
     * 标识是否是新创建的。
     */
    private boolean fresh;

    /**
     * 标识视图是否已解绑。
     */
    private boolean viewDetached = true;

    /**
     * 标识 {@link #detachView()} 是否被调用。
     */
    private boolean detachViewCalled;

    /**
     * 上次状态。
     */
    private State<V, M> lastState;

    /**
     * 上次状态是否已渲染。
     */
    private boolean stateRendered = true;

    /**
     * 标识是否是新的视图。
     */
    private boolean newView;

    /**
     * 状态机。
     */
    private StateMachine stateMachine;

    /**
     * 是否已暂停。
     */
    private boolean paused;

    /**
     * 标识状态是否已保存。
     */
    private boolean stateSaved;

    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArgs = superClass.getActualTypeArguments();
        viewProxy.setTargetInterface((Class<V>) typeArgs[0]);
        modelClass = (Class<M>) typeArgs[1];
        createModel();
        routerProxy.setTargetInterface((Class<R>) typeArgs[2]);
    }

    /**
     * 新增状态。
     *
     * @param state 新状态
     */
    protected void addState(State<? extends PassiveView, ? extends Model> state) {
        ensureStateMachine();
        stateMachine.addState(state);
    }

    /**
     * 渲染给定的状态。
     *
     * @param clazz 新状态类型
     */
    public void renderState(Class<?> clazz) {
        ensureStateMachine();
        State<V, M> state = stateMachine.getState(clazz);
        stateRendered = !stateSaved;
        if (stateRendered) {
            state.render(viewProxy.getTarget(), model);
        }
        lastState = state;
    }

    /**
     * 确保创建状态机。
     */
    private void ensureStateMachine() {
        if (stateMachine == null) {
            stateMachine = new StateMachine();
        }
    }

    /**
     * 创建表现模型。
     */
    protected void createModel() {
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取参数表。
     *
     * @return 参数表
     */
    protected StringMap getParams() {
        return getModel().getParams();
    }

    void attachView(PassiveView view, Router router, boolean isVisibleToUser) {
        try {
            viewProxy.setTarget((V) view);
        } catch (ClassCastException e) {
            throw new IllegalStateException(e);
        }
        try {
            routerProxy.setTarget((R) router);
        } catch (ClassCastException e) {
            throw new IllegalStateException(e);
        }
        viewDetached = false;
        onViewAttach(detachViewCalled, newView, isVisibleToUser);
        if (newView) {
            onNewViewAttach(detachViewCalled, isVisibleToUser);
        }
    }

    /**
     * 处理视图已绑定事件。
     *
     * @param reattach        是否是再次绑定
     * @param newView         是否是新视图
     * @param isVisibleToUser 是否对用户可见
     */
    protected void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        // 空实现
    }

    protected void onNewViewAttach(boolean reattach, boolean isVisibleToUser) {
        // 空实现
    }

    public void detachView() {
        viewDetached = true;
        detachViewCalled = true;
        viewProxy.setTarget(null);
        routerProxy.setTarget(null);
        onViewDetach();
    }

    protected void onViewDetach() {
        // 空实现
    }

    void setStateSaved(boolean called) {
        this.stateSaved = called;
    }

    /**
     * 创建。
     */
    void create() {
        newView = true;
        if (isFresh()) {
            onCreate();
        }
    }

    /**
     *
     */
    protected void onCreate() {

    }

    /**
     * 启动。
     */
    void start() {

    }

    /**
     * 恢复。
     *
     * @param visible 用户是否可见，true：可见；false：不可见。
     */
    void resume(boolean visible) {
        setStateSaved(false);
        if (!visible) {
            return;
        }
        boolean renderState = !stateRendered || newView;
        if (lastState != null && renderState) {
            lastState.render(viewProxy.getTarget(), model);
            stateRendered = true;
        }
        newView = false;
        onResume();
        paused = false;
    }

    protected void onResume() {

    }

    /**
     * 暂停。
     */
    void pause() {
        if (paused) {
            return;
        }
        onPause();
        paused = true;
    }

    protected void onPause() {

    }

    /**
     * 停止。
     */
    void stop() {

    }

    /**
     * 销毁。
     */
    public void destroy() {
        viewDetached = true;
        onDestroy();
    }

    protected void onDestroy() {

    }

    void setUserVisibleHint(boolean visible) {
        if (visible) {
            resume(true);
            onUserVisible();
            return;
        }
        pause();
        onUserInvisible();
    }

    /**
     * 当该用户界面对于用户来说是可见时被调用。
     */
    protected void onUserVisible() {
        // 空实现
    }

    /**
     * 当该用户界面对于用户来说是不可见时被调用。
     */
    protected void onUserInvisible() {
        // 空实现
    }

    /**
     * 获取视图。
     *
     * @return 视图
     */
    public V getView() {
        return viewProxy.getTarget();
    }

    /**
     * 获取路由器。
     *
     * @return 路由器
     */
    public R getRouter() {
        return routerProxy.getTarget();
    }

    /**
     * 设置表现模型。
     *
     * @param model 表现模型
     */
    public void setModel(M model) {
        this.model = model;
    }

    /**
     * 获取表现模型。
     *
     * @return 表现模型
     */
    public M getModel() {
        return model;
    }

    protected boolean isViewDetached() {
        return viewDetached;
    }

    void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    protected boolean isFresh() {
        return fresh;
    }
}
