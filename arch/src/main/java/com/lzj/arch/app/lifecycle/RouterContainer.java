/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.lifecycle;

import com.lzj.arch.core.Contract.Router;

/**
 * 该类代表路由器的容器。
 *
 * @author 吴吉林
 */
class RouterContainer<R extends Router> {

    /**
     * 路由器。
     */
    private R router;

    /**
     * 获取路由器。
     *
     * @return 路由器
     */
    public R getRouter() {
        return router;
    }

    /**
     * 设置路由器。
     *
     * @param router 路由器
     */
    public void setRouter(R router) {
        this.router = router;
    }
}
