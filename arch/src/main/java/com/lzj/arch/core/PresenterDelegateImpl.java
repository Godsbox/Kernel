/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lzj.arch.core.Contract.Presenter;

import static java.lang.System.currentTimeMillis;

/**
 * 该类实现了表现者委托接口。
 *
 * @author 吴吉林
 */
class PresenterDelegateImpl<P extends Presenter> implements PresenterDelegate<P> {

    /**
     * 键：表现者 ID。
     */
    private static final String KEY_PRESENTER_ID = "mvp_presenter_id";

    /**
     * 表现者 ID 前缀。
     */
    private static final String PRESENTER_ID_PREFIX = "presenter-";

    /**
     * 表现者。
     */
    private AbstractPresenter presenter;

    /**
     * 表现者代理。
     */
    private TargetProxy<P> presenterProxy = new TargetProxy<>();

    /**
     * 表现者 ID。
     */
    private String presenterId;

    /**
     * 标识 {@link #onSaveState(Bundle)} 方法是否已被调用。
     * 该标识将在 {@link #onViewCreate(Controller, Bundle, Bundle, PresenterManager)} 方法中重置。
     */
    private boolean saveStateCalled;

    /**
     * 标识表现者是否已被删除。
     */
    private boolean presenterRemoved;

    /**
     * 创建一个表现者委托。
     *
     * @param presenterInterface 表现者接口类型
     */
    PresenterDelegateImpl(Class<P> presenterInterface) {
        if (!presenterInterface.isInterface()) {
            throw new IllegalArgumentException("视图的表现者类型必须是一个接口，而不是类");
        }
        presenterProxy.setTargetInterface(presenterInterface);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreate(Controller<P> controller, Bundle state, Bundle params, PresenterManager manager) {
        if (state == null) {
            presenterId = PRESENTER_ID_PREFIX + currentTimeMillis();
        } else {
            presenterId = state.getString(KEY_PRESENTER_ID);
            saveStateCalled = false;
        }
        presenter = manager.getPresenter(presenterId, controller, params);
        presenterProxy.setTarget((P) presenter);
        presenterRemoved = false;
        presenter.create();
    }

    @Override
    public void attachView(Controller<P> controller, boolean isVisibleToUser) {
        if (presenter == null) {
            return;
        }
        presenter.attachView(controller, controller.getRouter(), isVisibleToUser);
    }

    @Override
    public void detachView() {
        if (presenter == null) {
            return;
        }
        presenter.detachView();
    }

    @Override
    public void onSaveState(Bundle state) {
        if (presenter == null) {
            return;
        }
        state.putString(KEY_PRESENTER_ID, presenterId);
        saveStateCalled = true;
        presenter.setStateSaved(true);
    }

    @Override
    public void onViewResume(boolean isVisibleToUser) {
        if (presenter == null) {
            return;
        }
        presenter.resume(isVisibleToUser);
    }

    @Override
    public void onViewPause() {
        if (presenter == null) {
            return;
        }
        presenter.pause();
    }

    @Override
    public void onViewDestroy(Fragment fragment) {
        if (presenter == null) {
            return;
        }
        if (fragment.getActivity().isFinishing()) {
            removePresenter();
        } else if (fragment.isRemoving() && !saveStateCalled) {
            removePresenter();
        }
    }

    @Override
    public void onViewDestroy(Activity activity) {
        if (presenter == null) {
            return;
        }
        if (activity.isFinishing()) {
            removePresenter();
        }
    }

    @Override
    public void onLayoutDestroy(Activity activity) {
        if (presenter == null) {
            return;
        }
        if (activity.isFinishing()) {
            removePresenter();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (presenter == null) {
            return;
        }
        presenter.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 获取表现者。
     *
     * @return 表现者
     */
    @Override
    @SuppressWarnings("unchecked")
    public P getPresenter() {
        return presenterProxy.getTarget();
    }

    @Override
    public P createPresenter() {
        String implName = presenterProxy.getTargetInterface().getName().replace("Contract$", "");
        try {
            AbstractPresenter presenter = (AbstractPresenter) Class.forName(implName).newInstance();
            return (P) presenter;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除表现者。
     */
    private void removePresenter() {
        if (presenterRemoved) {
            return;
        }
        PresenterManager.getDefault().remove(presenterId);
        presenter.destroy();
        presenter = null;
        presenterProxy.setTarget(null);
        presenterRemoved = true;
    }
}
