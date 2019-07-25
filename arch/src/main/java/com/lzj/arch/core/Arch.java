/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import com.lzj.arch.core.Contract.PassiveView;
import com.lzj.arch.core.Contract.Presenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.lzj.arch.util.ObjectUtils.requireNonNull;
import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * 代表架构的类。
 *
 * @author 吴吉林
 */
public final class Arch {
    /**
     * 空调用处理者。
     */
    private static final InvocationHandler NULL_INVOCATION_HANDLER = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    };

    /**
     * 空表现者委托。
     */
    private static final PresenterDelegate NULL_PRESENTER_DELEGATE = newProxy(PresenterDelegate.class);

    /**
     * 私有构造方法。
     */
    private Arch() {
    }

    /**
     * 根据视图类别获取表现者类别。
     *
     * @param view 视图
     * @param <P>  表现者类型
     * @return 表现者类别
     */
    @SuppressWarnings("unchecked")
    private static <P extends Presenter> Class<P> getPresenterInterface(PassiveView view) {
        requireNonNull(view, "给定的视图对象不能为 null");
        Type superclass = view.getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return null;
        }
        Type[] typeArgs = ((ParameterizedType) superclass).getActualTypeArguments();
        Type presenterType = typeArgs[0];
        if (!(presenterType instanceof Class)) {
            throw new IllegalArgumentException("The View's presenter interface must not be parameterized.");
        }
        return (Class<P>) presenterType;
    }

    /**
     * 创建表现者委托。
     *
     * @param view 视图
     * @param <P>  表现者接口
     * @return 表现者委托
     */
    @SuppressWarnings("unchecked")
    public static <P extends Presenter> PresenterDelegate<P> newPresenterDelegate(PassiveView view) {
        Class<P> presenterInterface = getPresenterInterface(view);
        boolean presenterOff = presenterInterface == Presenter.class; // 表现者功能是否关闭
        if (presenterOff) {
            return (PresenterDelegate<P>) NULL_PRESENTER_DELEGATE;
        }
        return new PresenterDelegateImpl<>(presenterInterface);
    }

    /**
     * 创建一个新的动态代理。
     *
     * @param proxyInterface 代理要实现的接口类型
     * @param <P> 代理要实现的接口
     * @return 动态代理
     */
    @SuppressWarnings("unchecked")
    static <P> P newProxy(Class<P> proxyInterface) {
        return (P) newProxyInstance(Arch.class.getClassLoader(),
                new Class[] { proxyInterface }, NULL_INVOCATION_HANDLER);
    }
}
