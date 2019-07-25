/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import com.lzj.arch.core.Contract.PassiveView;
import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.core.Contract.Router;

/**
 * 视图控制器。<br /><br />
 *
 * 另外去定义这个新接口，而没有把下面的接口方法定义在 {@link PassiveView} 里，是为了禁止在 {@link Presenter}
 *
 * 里去调用下面的接口方法。
 *
 * @author 吴吉林
 */
public interface Controller<P extends Presenter> extends PassiveView {

    /**
     * 创建表现者。
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

    /**
     * 获取路由器。
     *
     * @return 路由器
     */
    Router getRouter();
}
