/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lzj.arch.core.Contract.Presenter;

/**
 * 代表表现者委托接口。
 *
 * @author 吴吉林
 */
public interface PresenterDelegate<P extends Presenter> {

    void onViewCreate(Controller<P> controller, Bundle state, Bundle params, PresenterManager manager);

    void attachView(Controller<P> controller, boolean isVisibleToUser);

    void detachView();

    void onSaveState(Bundle state);

    void onViewResume(boolean isVisibleToUser);

    void onViewPause();

    void onViewDestroy(Fragment fragment);

    void onViewDestroy(Activity activity);

    void onLayoutDestroy(Activity activity);

    void setUserVisibleHint(boolean isVisibleToUser);

    /**
     * 创建表现者和表现模型。
     *
     * @return 新创建的表现者
     */
    P createPresenter();

    /**
     * 获取表现者。
     *
     * @return 表现者
     */
    P getPresenter();
}
