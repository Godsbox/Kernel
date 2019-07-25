/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lzj.arch.app.PassiveActivity;
import com.lzj.arch.core.Contract.Router;

/**
 * 屏幕生命周期。
 *
 * @author 吴吉林
 */
public class ActivityLifecycle<R extends Router> extends RouterContainer<R> {

    public boolean isSchemeIntent(String scheme) {
        return false;
    }

    public void onCreate(PassiveActivity activity, Bundle state) {}

    public void onStart(PassiveActivity activity) {}

    public void onResume(PassiveActivity activity) {}

    public void onPause(PassiveActivity activity) {}

    public void onStop(PassiveActivity activity) {}

    public void onSaveState(PassiveActivity activity, Bundle state) {}

    public void onDestroy(PassiveActivity activity) {}

    public void onPostCreate(PassiveActivity activity, @Nullable Bundle state) {}

    public void onFinish(PassiveActivity activity) {}

    public void onActivityResult(PassiveActivity activity, int requestCode, int resultCode, Intent data) {

    }
}
