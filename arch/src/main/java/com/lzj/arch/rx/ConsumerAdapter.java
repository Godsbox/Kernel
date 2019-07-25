/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.rx;

import io.reactivex.functions.Consumer;

/**
 * 观察者适配器
 * @author wsy
 * @time 2018/6/14 14:35
 */
public class ConsumerAdapter<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
    }
}
