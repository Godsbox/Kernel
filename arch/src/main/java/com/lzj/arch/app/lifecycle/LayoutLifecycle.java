/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

import android.app.Activity;

import com.lzj.arch.core.Contract;

/**
 * 布局生命周期。
 *
 * @author 吴吉林
 */
public class LayoutLifecycle<R extends Contract.Router> extends RouterContainer<R> {

    public void onCreate(Activity activity) {}
}
