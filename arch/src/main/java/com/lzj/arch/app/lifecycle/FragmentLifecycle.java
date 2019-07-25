/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

import android.os.Bundle;

import com.lzj.arch.app.PassiveFragment;
import com.lzj.arch.core.Contract;

/**
 * 界面生命周期。
 *
 * @author 吴吉林
 */
public class FragmentLifecycle<R extends Contract.Router> extends RouterContainer<R> {
    public void onCreate(PassiveFragment fragment, Bundle state) {}

    public void onStart(PassiveFragment fragment) {}

    public void onResume(PassiveFragment fragment, boolean visible) {}

    public void onUserVisibleChange(PassiveFragment fragment, boolean visible) {}

    public void onPause(PassiveFragment fragment, boolean visible) {}

    public void onStop(PassiveFragment fragment) {}

    public void onSaveState(PassiveFragment fragment, Bundle state) {}

    public void onDestroy(PassiveFragment fragment) {}
}
