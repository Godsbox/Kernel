/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import static com.lzj.arch.core.Arch.newProxy;

/**
 * 目标代理。
 *
 * @param <T> 目标接口类型
 *
 * @author 吴吉林
 */
public class TargetProxy<T> {

    /**
     * 目标。
     */
    private T target;

    /**
     * 代理。
     */
    private T proxy;

    /**
     * 目标接口类型。
     */
    private Class<T> targetInterface;

    public void setTargetInterface(Class<T> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public Class<T> getTargetInterface() {
        return targetInterface;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public T getTarget() {
        if (target != null) {
            return target;
        }
        if (proxy == null) {
            proxy = newProxy(targetInterface);
        }
        return proxy;
    }
}
