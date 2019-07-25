/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusBuilder;

import static de.greenrobot.event.EventBus.getDefault;

/**
 * 事件总线管理者。<br /><br />
 *
 * 负责管理三条事件总线：默认、UI、响应，其中后2条总线发布事件总是发生在主线程里。
 *
 * @author 吴吉林
 */
public final class BusManager {

    /**
     * UI 事件总线。
     */
    private static Bus UI_BUS;

    /**
     * 响应事件总线。
     */
    private static Bus RESPONSE_BUS;

    /**
     * 应用（默认）事件总线。
     */
    private static Bus APP_BUS = new BusWrapper(getDefault());

    /**
     * 静态初始化，创建应用所需事件总线。
     */
    static {
        EventBusBuilder builder = EventBus.builder().eventInheritance(false);
        UI_BUS = new MainBus(builder.build());
        RESPONSE_BUS = new MainBus(builder.build());
    }

    /**
     * 获取应用（默认）事件总线。
     *
     * @return 应用（默认）事件总线
     */
    public static Bus getAppBus() {
        return APP_BUS;
    }

    /**
     * 发布 UI 事件。
     *
     * @param event 事件
     */
    public static void postUi(Object event) {
        UI_BUS.post(event);
    }

    /**
     * 发布 UI 置顶事件。
     *
     * @param event 事件
     */
    public static void postUiSticky(Object event) {
        UI_BUS.postSticky(event);
    }

    /**
     * 获取指定事件类型的 UI 置顶事件。
     *
     * @param eventType 事件类型
     * @param <T>       事件类型
     * @return 置顶事件
     */
    public static <T> T getUiSticky(Class<T> eventType) {
        return UI_BUS.getStickyEvent(eventType);
    }

    /**
     * 删除指定事件类型的 UI 置顶事件。
     *
     * @param eventType 事件类型
     * @param <T>       事件类型
     * @return 已删除的置顶事件
     */
    public static <T> T removeUiSticky(Class<T> eventType) {
        return UI_BUS.removeStickyEvent(eventType);
    }

    /**
     * 删除 UI 置顶事件。
     *
     * @param event 置顶事件
     * @return true：成功删除；false：删除失败。
     */
    public static boolean removeUiSticky(Object event) {
        return UI_BUS.removeStickyEvent(event);
    }

    /**
     * 发布响应事件。
     *
     * @param event 事件
     */
    public static void postResponse(Object event) {
        RESPONSE_BUS.post(event);
    }

    /**
     * 发布响应置顶事件。
     *
     * @param event 事件
     */
    public static void postResponseSticky(Object event) {
        RESPONSE_BUS.postSticky(event);
    }

    /**
     * 获取指定事件类型的响应置顶事件。
     *
     * @param eventType 事件类型
     * @param <T>       事件类型
     * @return 置顶事件
     */
    public static <T> T getResponseSticky(Class<T> eventType) {
        return RESPONSE_BUS.getStickyEvent(eventType);
    }

    /**
     * 删除指定事件类型的响应置顶事件。
     *
     * @param eventType 事件类型
     * @param <T>       事件类型
     * @return 已删除的置顶事件
     */
    public static <T> T removeResponseSticky(Class<T> eventType) {
        return RESPONSE_BUS.removeStickyEvent(eventType);
    }

    /**
     * 删除响应置顶事件。
     *
     * @param event 置顶事件
     * @return true：成功删除；false：删除失败。
     */
    public static boolean removeResponseSticky(Object event) {
        return RESPONSE_BUS.removeStickyEvent(event);
    }

    /**
     * 注册 UI 事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @return UI 事件总线
     */
    public static Bus registerUi(Object subscriber) {
        return registerUi(subscriber, false);
    }

    /**
     * 注册 UI 事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @param sticky     是否置顶。true：是；false：否。
     * @return UI 事件总线
     */
    public static Bus registerUi(Object subscriber, boolean sticky) {
        if (sticky) {
            UI_BUS.registerSticky(subscriber);
            return UI_BUS;
        }
        UI_BUS.register(subscriber);
        return UI_BUS;
    }

    /**
     * 注销 UI 事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @return UI 事件总线
     */
    public static Bus unregisterUi(Object subscriber) {
        UI_BUS.unregister(subscriber);
        return UI_BUS;
    }

    /**
     * 注册响应事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @return 响应事件总线
     */
    public static Bus registerResponse(Object subscriber) {
        return registerResponse(subscriber, false);
    }

    /**
     * 注册响应事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @param sticky     是否置顶。true：是；false：否。
     * @return 响应事件总线
     */
    public static Bus registerResponse(Object subscriber, boolean sticky) {
        if (sticky) {
            RESPONSE_BUS.registerSticky(subscriber);
            return RESPONSE_BUS;
        }
        RESPONSE_BUS.register(subscriber);
        return RESPONSE_BUS;
    }

    /**
     * 注销响应事件总线订阅者。
     *
     * @param subscriber 订阅者
     * @return 响应事件总线
     */
    public static Bus unregisterResponse(Object subscriber) {
        RESPONSE_BUS.unregister(subscriber);
        return RESPONSE_BUS;
    }
}
