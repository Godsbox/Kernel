/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

/**
 * 事件总线接口。
 *
 * @author 吴吉林
 */
public interface Bus {

    /**
     * 获取指定类型的置顶事件。
     *
     * @param eventType 事件类型
     * @param <T>       事件类型
     * @return 置顶事件
     */
    <T> T getStickyEvent(Class<T> eventType);

    /**
     * 判断是否有订阅指定类型的订阅者。
     *
     * @param eventClass 事件类型
     * @return true：有；false：没有。
     */
    boolean hasSubscriberForEvent(Class<?> eventClass);

    /**
     * 发布事件。
     *
     * @param event 待发布的事件
     */
    void post(Object event);

    /**
     * 发布置顶事件。
     *
     * @param event 待发布的置顶事件
     */
    void postSticky(Object event);

    /**
     * 注册订阅者。
     *
     * @param subscriber 订阅者
     */
    void register(Object subscriber);

    /**
     * 取消注册订阅者。
     *
     * @param subscriber 订阅者
     */
    void unregister(Object subscriber);

    /**
     * 注册置顶订阅者。
     *
     * @param subscriber 订阅者
     */
    void registerSticky(Object subscriber);

    /**
     * 删除指定类型的置顶事件。
     *
     * @param eventType 置顶事件类型
     * @param <T>       置顶事件类型
     * @return 置顶事件
     */
    <T> T removeStickyEvent(Class<T> eventType);

    /**
     * 删除置顶事件。
     *
     * @param event 置顶事件
     * @return true：删除成功；false：删除失败。
     */
    boolean removeStickyEvent(Object event);

    /**
     * 删除所有的置顶事件。
     */
    void removeAllStickyEvents();

    /**
     * 取消事件传递。
     *
     * @param event 事件
     */
    void cancelEventDelivery(Object event);
}
