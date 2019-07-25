/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

import android.os.Handler;

import com.lzj.arch.util.LooperUtils;

import de.greenrobot.event.EventBus;

import static android.os.Looper.getMainLooper;

/**
 * 主线程事件总线。<br /><br />
 *
 * 发布事件总是发生在主线程里。
 *
 * @author 吴吉林
 */
class MainBus extends BusWrapper {

    /**
     * 主线程消息处理者。
     */
    private static final Handler MAIN_HANDLER = new Handler(getMainLooper());

    public MainBus(EventBus bus) {
        super(bus);
    }

    @Override
    public void post(final Object event) {
        if (LooperUtils.inMainThread()) {
            super.post(event);
            return;
        }
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainBus.super.post(event);
            }
        });
    }

    @Override
    public void postSticky(final Object event) {
        super.postSticky(event);
        if (LooperUtils.inMainThread()) {
            super.postSticky(event);
            return;
        }
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                MainBus.super.postSticky(event);
            }
        });
    }
}
