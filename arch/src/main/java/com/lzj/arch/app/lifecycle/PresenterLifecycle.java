/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

import com.lzj.arch.app.PassivePresenter;

/**
 * 表现者生命周期。
 *
 * @author 吴吉林
 */
public class PresenterLifecycle {
    public void onCreate(PassivePresenter presenter) {}
    public void onViewAttach(PassivePresenter presenter, boolean reattach) {}
    public void onResume(PassivePresenter presenter) {}
    public void onPause(PassivePresenter presenter) {}
    public void onViewDetach(PassivePresenter presenter) {}
    public void onDestroy(PassivePresenter presenter) {}
}
