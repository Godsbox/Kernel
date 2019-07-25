/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.rx;

import io.reactivex.observers.DisposableObserver;

/**
 * 观察者适配器。
 *
 * @author 吴吉林
 */
public class ObserverAdapter<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        // 空实现
    }

    @Override
    public final void onError(Throwable t) {
        ObservableException e = t instanceof ObservableException
                ? (ObservableException) t
                : new ObservableException(t.getMessage());
        onError(e);
    }

    /**
     * 处理错误。
     *
     * @param e 错误
     */
    protected void onError(ObservableException e) {
        // 空实现
    }

    @Override
    public void onComplete() {
        // 空实现
    }
}
