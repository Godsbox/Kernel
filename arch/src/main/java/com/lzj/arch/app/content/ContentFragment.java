/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnChildScrollUpCallback;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;

import com.lzj.arch.R;
import com.lzj.arch.app.PassiveFragment;
import com.lzj.arch.app.content.ContentContract.PassiveView;
import com.lzj.arch.app.content.ContentContract.Presenter;

import static com.lzj.arch.util.ToastUtils.showShort;

/**
 * 内容界面。
 *
 * @author 吴吉林
 */
public abstract class ContentFragment<P extends Presenter>
        extends PassiveFragment<P>
        implements PassiveView, ContentLoadListener, OnRefreshListener, OnChildScrollUpCallback {

    /**
     * 内容刷新布局。
     */
    private SwipeRefreshLayout refreshLayout;

    /**
     * 内容加载视图。
     */
    private ContentLoadView loadView;

    /**
     * 加载视图的容器。
     */
    private View loadViewContainer;

    /**
     * 内容配置。
     */
    private ContentConfig contentConfig;

    /**
     * 副内容配置。
     */
    private ContentConfig contentConfigSecondary;

    @CallSuper
    @Override
    public void onFindView() {
        super.onFindView();
        refreshLayout = findView(R.id.refresh_layout);
        loadViewContainer = findView(R.id.load_view_container);
    }

    @CallSuper
    @Override
    public void onInitView(Bundle state) {
        super.onInitView(state);
        initRefreshLayout();
    }

    /**
     * 初始化刷新布局。
     */
    private void initRefreshLayout() {
        if (refreshLayout == null) {
            return;
        }
        refreshLayout.setColorSchemeResources(R.color.primary);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnChildScrollUpCallback(this);
    }

    /**
     * 获取内容配置。
     *
     * @return 内容配置
     */
    protected ContentConfig getContentConfig() {
        if (contentConfig == null) {
            contentConfig = new ContentConfig();
        }
        return contentConfig;
    }

    /**
     * 获取副内容配置。
     *
     * @return 副内容配置
     */
    protected ContentConfig getContentConfigSecondary() {
        if (contentConfigSecondary == null) {
            contentConfigSecondary = new ContentConfig();
        }
        return contentConfigSecondary;
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadView = new ContentLoadView(getActivity());
        loadView.setContentLoadListener(this);
    }

    @Override
    public void showLoading() {
        loadView.showStart(getLoadViewContainer());
    }

    @Override
    public void hideLoading() {
        loadView.dismiss(getLoadViewContainer());
    }

    @Override
    public void showLoadFailure(CharSequence errorMessage) {
        loadView.setErrorMessage(errorMessage);
        loadView.showFailure(getLoadViewContainer());
    }

    @Override
    public void showLoadEmpty() {
        if (contentConfig != null) {
            loadView.setEmpty(contentConfig.getEmptyTitle(), contentConfig.getEmptyMessage(),
                    contentConfig.getEmptyImage(), contentConfig.getEmptyAction());
            loadView.setNeedLoginView(contentConfig.isNeedLogin() && !getPresenter().isLogin());
        }
        loadView.showEmpty(getLoadViewContainer());
    }

    @Override
    public void showLoadEmptySecondary(CharSequence title) {
        ContentConfig contentConfig = contentConfigSecondary == null
                ? this.contentConfig : contentConfigSecondary;
        if (contentConfig != null) {
            loadView.setEmpty(title, contentConfig.getEmptyMessage(),
                    contentConfig.getEmptyImage(), contentConfig.getEmptyAction());
            loadView.setNeedLoginView(contentConfig.isNeedLogin() && !getPresenter().isLogin());
        }
        loadView.showEmpty(getLoadViewContainer());
    }

    protected View getLoadViewContainer() {
        if (loadViewContainer == null) {
            return getView();
        }
        return loadViewContainer;
    }

    @Override
    public void setRefreshEnabled(boolean enabled) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(enabled);
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(refreshing);
        }
    }

    @Override
    public void showRefreshFailure(String message) {
        showShort(message);
    }

    @Override
    public void showRefreshEmpty() {

    }

    @Override
    public void onReloadClick() {
        getPresenter().onReloadClick();
    }

    @Override
    public void onEmptyClick() {
        getPresenter().onEmptyClick();
    }

    @Override
    public void onDiyAction() {
        getPresenter().onDiyClick();
    }

    @Override
    public final void onRefresh() {
        getPresenter().onRefreshTrigger();
    }

    @Override
    public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
        return false;
    }

}
