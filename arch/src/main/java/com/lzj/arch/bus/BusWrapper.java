/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

import de.greenrobot.event.EventBus;

/**
 * EventBus 包装器。<br /><br />
 *
 * 该类是 {@link Bus} 接口的 EventBus 实现。
 *
 * @author 吴吉林
 */
class BusWrapper implements Bus {

    /**
     * 被包装的事件总线。
     */
    private EventBus bus;

    public BusWrapper(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public <T> T getStickyEvent(Class<T> eventType) {
        return bus.getStickyEvent(eventType);
    }

    @Override
    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        return bus.hasSubscriberForEvent(eventClass);
    }

    @Override
    public void post(Object event) {
        if (event == null) {
            return;
        }
        bus.post(event);
    }

    @Override
    public void postSticky(Object event) {
        if (event == null) {
            return;
        }
        bus.postSticky(event);
    }

    @Override
    public void register(Object subscriber) {
        if (bus.isRegistered(subscriber)) {
            return;
        }
        bus.register(subscriber);
    }

    @Override
    public void registerSticky(Object subscriber) {
        if (bus.isRegistered(subscriber)) {
            return;
        }
        bus.registerSticky(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        if (bus.isRegistered(subscriber)) {
            bus.unregister(subscriber);
        }
    }

    @Override
    public <T> T removeStickyEvent(Class<T> eventType) {
        return bus.removeStickyEvent(eventType);
    }

    @Override
    public boolean removeStickyEvent(Object event) {
        return bus.removeStickyEvent(event);
    }

    @Override
    public void removeAllStickyEvents() {
        bus.removeAllStickyEvents();
    }

    @Override
    public void cancelEventDelivery(Object event) {
        bus.cancelEventDelivery(event);
    }
}