/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.web;

import android.support.annotation.CallSuper;

import com.lzj.arch.app.content.ContentObserver;
import com.lzj.arch.app.content.ContentPresenter;
import com.lzj.arch.app.content.state.LoadFailure;
import com.lzj.arch.app.web.WebContract.PassiveView;
import com.lzj.arch.app.web.WebContract.Presenter;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.util.StringUtils;

import static com.lzj.arch.app.web.WebConstant.EXTRA_TITLE;
import static com.lzj.arch.rx.ObservableException.ERROR_CODE_NONE;

/**
 * 网页内容表现者。
 *
 * @author 吴吉林
 */
public class WebPresenter<V extends PassiveView, M extends WebModel, R extends Router>
        extends ContentPresenter<V, M, R>
        implements Presenter {

    /**
     * 内容观察者。
     */
    private ContentObserver<Object> contentObserver = new ContentObserver<>(this);

    /**
     * 标识是否调用 {@link #onPageEnd(String)} 方法。<br /><br />
     * <p>
     * 在无网络下情况下重新加载网页，会出现以下意外的调用顺序：<br />
     * 1. {@link #onPageError(int, String, String)}；<br />
     * 2. {@link #onPageEnd(String)}；<br />
     * 3. {@link #onPageStart(String)}。<br />
     * <p>
     * 因此额外加一个标识来解决该怪异现象。
     */
    private boolean onPageEndCalled;

    public void setOnPageEndCalled(boolean onPageEndCalled) {
        this.onPageEndCalled = onPageEndCalled;
    }

    @Override
    protected void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        super.onViewAttach(reattach, newView, isVisibleToUser);
        if (reattach && newView) {
            load();
        }
    }

    @Override
    protected void doLoad() {
        onPageEndCalled = false;
        getView().load(getModel().getUrl());
    }

    @Override
    protected void doRefresh() {
        onPageEndCalled = false;
        getView().refresh();
    }

    @CallSuper
    @Override
    public void onPageStart(String url) {
        if (onPageEndCalled) {
            return;
        }
        getView().setRefreshEnabled(false);
        if (getModel().isErrorReceived()) {
            getView().hideLoading();
        }
        getModel().setErrorReceived(false);
    }

    @CallSuper
    @Override
    public void onPageEnd(String url) {
        onPageEndCalled = true;
        getView().setProgressVisible(false);
        if (getModel().isErrorReceived()) {
            renderState(LoadFailure.class);
            return;
        }
        getView().setRefreshEnabled(getModel().isRefreshEnabled());
        getModel().setError(ERROR_CODE_NONE, "");
        contentObserver.onNext("");
    }

    @CallSuper
    @Override
    public void onPageError(int errorCode, String message, String url) {
        getModel().setErrorReceived(true);
        getModel().setError(errorCode, message);
    }

    @Override
    public void onTitleReceived(String title) {
        String webTitle = getParams().getString(EXTRA_TITLE);
        if (!StringUtils.isEmpty(webTitle)) {
            title = webTitle;
        }
        getView().setTitle(title);
    }
}
