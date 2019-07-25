/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import android.support.annotation.CallSuper;

import com.lzj.arch.app.PassivePresenter;
import com.lzj.arch.app.content.ContentContract.PassiveView;
import com.lzj.arch.app.content.ContentContract.Presenter;
import com.lzj.arch.app.content.state.LoadEmpty;
import com.lzj.arch.app.content.state.LoadFailure;
import com.lzj.arch.app.content.state.LoadStart;
import com.lzj.arch.app.content.state.RefreshEmpty;
import com.lzj.arch.app.content.state.RefreshFailure;
import com.lzj.arch.app.content.state.RefreshStart;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.core.State;

import static com.lzj.arch.app.content.ContentModel.TYPE_LOAD;
import static com.lzj.arch.app.content.ContentModel.TYPE_REFRESH;

/**
 * 内容表现者。
 *
 * @author 吴吉林
 */
public abstract class ContentPresenter
        <V extends PassiveView, M extends ContentModel, R extends Router>
        extends PassivePresenter<V, M, R>
        implements Presenter {

    {
        addState(new LoadStart());
        addState(new LoadSuccessState());
        addState(new LoadFailure());
        addState(new LoadEmpty());
        addState(new RefreshStart());
        addState(new RefreshFailure());
        addState(new RefreshEmpty());
        addState(new RefreshState());
        addState(new ReloadState());
    }

    @CallSuper
    @Override
    protected void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        super.onViewAttach(reattach, newView, isVisibleToUser);
        if (newView) {
            getView().setRefreshEnabled(getModel().isRefreshEnabled());
        }

        // 首次绑定视图
        if (!reattach) {
            getModel().setLazyLoad(!isVisibleToUser);
            if (isVisibleToUser) {
                load();
            }
            return;
        }

        // 新试图并且重新绑定时展示已有的内容。
        // TODO 呈现最近一次的内容状态。
        if (!getModel().isEmpty() && newView) {
            onContentLoaded();
        }

        // 再次绑定视图
        if (isVisibleToUser && getModel().isLazyLoad()) {
            getModel().setLazyLoad(false);
            load();
        }
    }

    @Override
    protected void onUserVisible() {
        if (getModel().isLazyLoad()) {
            load();
            getModel().setLazyLoad(false);
        }
    }

    /**
     * 加载内容。
     */
    protected void load() {
        getModel().setType(TYPE_LOAD);
        renderState(LoadStart.class);
        doLoad();
    }

    /**
     * 刷新内容。
     */
    public void refresh() {
        renderState(RefreshStart.class);
        onRefreshTrigger();
    }

    @Override
    public void onReloadClick() {
        load();
    }

    @Override
    public void onRefreshTrigger() {
        getModel().setType(TYPE_REFRESH);
        renderState(RefreshStart.class);
        doRefresh();
    }

    @Override
    public void onDiyClick() {
        goLogin();
    }

    /**
     * 判断是否是刷新请求。
     *
     * @return true：刷新请求；false：非刷新请求。
     */
    protected boolean isRefreshType() {
        return getModel().getType() == TYPE_REFRESH;
    }

    /**
     * 判断是否是需要不考虑缓存加载。
     *
     * @return true：刷新请求；false：非刷新请求。
     */
    protected boolean isNeedRefreshData() {
        return getModel().getType() == TYPE_REFRESH || getModel().isError();
    }

    /**
     * 订阅并处理更新内容事件。
     *
     * @param event 更新内容事件
     */
    public void onEvent(UpdateContentEvent event) {
        if (!getModel().isFreshContentEnabled()) {
            return;
        }
        if (!event.isForAll()
                && !getClass().getName().equals(event.getClassName())) {
            return;
        }
        if (!getModel().isLazyLoad()) {
            renderState(event.isRefresh() ? RefreshState.class : ReloadState.class);
        }
    }

    @Override
    public void onEmptyClick() {
        // 空实现
    }

    /**
     * 执行内容加载。<br /><br />
     * <p>
     * 子类通过实现该方法加载内容。
     */
    protected void doLoad() {
    }

    /**
     * 执行内容刷新。<br /><br />
     * <p>
     * 默认实现是调用 {@link #doLoad()}。
     */
    protected void doRefresh() {
        doLoad();
    }

    /**
     * 处理内容已加载事件。
     */
    protected void onContentLoaded() {
        // 空实现
    }

    /**
     * 执行自定义操作。<br /><br />
     * <p>
     */
    protected void goLogin() {
        // 空实现
    }

    /**
     * 获取登陆状态。
     */
    @Override
    public boolean isLogin() {
        // 空实现 需要的再重写
        return false;
    }

    /**
     * 处理内容未加载状态。<br /><br />
     *
     * 当内容加载失败或内容为空时调用该方法。
     *
     * @param empty true：内容为空；false：加载失败。
     */
    protected void onContentUnloaded(boolean empty) {
        // 空实现
    }

    /**
     * 加载成功状态。
     *
     * @author 吴吉林
     */
    class LoadSuccessState implements State<PassiveView, ContentModel> {

        @Override
        public void render(PassiveView view, ContentModel model) {
            view.setRefreshing(false);
            view.hideLoading();
            onContentLoaded();
        }
    }

    /**
     * 刷新内容状态。
     */
    private class RefreshState implements State<PassiveView, ContentModel> {
        @Override
        public void render(PassiveView view, ContentModel model) {
            refresh();
        }
    }

    /**
     * 重新加载内容状态。
     */
    private class ReloadState implements State<PassiveView, ContentModel> {
        @Override
        public void render(PassiveView view, ContentModel model) {
            load();
        }
    }
}
