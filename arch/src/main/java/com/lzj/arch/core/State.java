/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import com.lzj.arch.core.Contract.PassiveView;

/**
 * 表现状态。<br /><br />
 *
 * @author 吴吉林
 */
public interface State<V extends PassiveView, M extends Model> {

    /**
     * 渲染该状态。
     *
     * @param view 视图
     * @param model 表现模型
     */
    void render(V view, M model);
}
