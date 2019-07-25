/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

/**
 * 视图与表现者相互通讯契约。
 *
 * @author 吴吉林
 */
public interface Contract {

    /**
     * 视图接口。
     */
    interface PassiveView {
    }

    /**
     * 表现者接口。
     */
    interface Presenter {
    }

    /**
     * 路由器接口。
     */
    interface Router {
    }
}
