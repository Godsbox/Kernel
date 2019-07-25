/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.rx;

import android.os.Looper;
import android.view.View;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;
import android.annotation.SuppressLint;
import io.reactivex.annotations.NonNull;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import android.support.annotation.CheckResult;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * RxJava 工具类
 * @author wsy
 * @time 2019/4/29 10:56
 */
public final class RxJavaUtils {

    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 防止重复点击
     *
     * @param view 目标view
     * @param action 监听器
     */
    public static void setOnClickListener(final ClickAction action, @NonNull View view) {
        RxJavaUtils.onClick(view).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<View>() {
            @Override
            public void accept(@NonNull View view) {
                action.onClick(view);
            }
        });
    }

    /**
     * 防止重复点击
     *
     * @param target 目标view
     * @param action 监听器
     */
    public static void setOnClickListeners(final ClickAction action, @NonNull View... target) {
        for (View view : target) {
            RxJavaUtils.onClick(view).throttleFirst(1500, TimeUnit.MILLISECONDS).subscribe(new Consumer<View>() {
                @Override
                public void accept(@NonNull View view) {
                    action.onClick(view);
                }
            });
        }
    }

    /**
     * 监听onclick事件防抖动
     *
     * @param view
     * @return
     */
    @SuppressLint("RestrictedApi")
    @CheckResult
    @NonNull
    private static Observable onClick(@NonNull View view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ViewClickOnSubscribe(view));
    }

    /**
     * onclick事件防抖动
     * 返回view
     */
    private static class ViewClickOnSubscribe implements ObservableOnSubscribe {
        private View view;

        public ViewClickOnSubscribe(View view) {
            this.view = view;
        }

        @Override
        public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter e) throws Exception {
            checkUiThread();

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!e.isDisposed()) {
                        e.onNext(view);
                    }
                }
            };
            view.setOnClickListener(listener);
        }
    }

    public static void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }

    /**
     * A one-argument action. 点击事件转发接口
     */
    public interface ClickAction<T> {
        void onClick(T t);
    }
}
