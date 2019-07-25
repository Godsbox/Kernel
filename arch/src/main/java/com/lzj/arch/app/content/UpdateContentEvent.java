/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import com.lzj.arch.bus.BusManager;
import com.lzj.arch.util.StringUtils;

/**
 * 更新内容事件。
 *
 * @author 吴吉林
 */
public final class UpdateContentEvent {

    /**
     * 内容更新方法，true：刷新；false：重新加载。
     */
    private boolean refresh;

    /**
     * 试图关闭方法，true：关闭；false：不关闭。
     */
    private boolean isClose;

    /**
     * 关闭位置。
     */
    private int closePosition;

    /**
     * 类型名称。
     */
    private String className;

    /**
     * 私有构造方法。
     */
    private UpdateContentEvent() {
    }

    /**
     * 构造一个新的发送给所有订阅者的更新内容事件。
     *
     * @return 更新内容事件
     */
    public static UpdateContentEvent forAll() {
        UpdateContentEvent event = new UpdateContentEvent();
        event.className = null;
        return event;
    }

    /**
     * 构造一个新的发送给所有订阅者的更新内容事件。
     *
     * @return 更新内容事件
     */
    public static UpdateContentEvent forUiClose(int position) {
        UpdateContentEvent event = new UpdateContentEvent();
        event.className = null;
        event.isClose = true;
        event.closePosition = position;
        return event;
    }

    /**
     * 构造一个新的发送给特定类型订阅者的更新内容事件。
     *
     * @param clazz 类型
     * @return 更新内容事件
     */
    public static UpdateContentEvent forClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz 不能为null");
        }
        UpdateContentEvent event = new UpdateContentEvent();
        event.className = clazz.getName();
        return event;
    }

    /**
     * 设置更新内容方法。
     *
     * @param refreshOrLoad true：刷新；false：加载。
     * @return this
     */
    public UpdateContentEvent method(boolean refreshOrLoad) {
        refresh = refreshOrLoad;
        return this;
    }

    /**
     * 发布该事件。
     */
    public void post() {
        BusManager.postResponse(this);
    }

    /**
     * 发布该事件。
     */
    public void postUi() {
        BusManager.postUi(this);
    }

    /**
     * 获取类型名称。
     *
     * @return 类型名称
     */
    String getClassName() {
        return className;
    }

    /**
     * 判断是否是发给所有订阅者。
     *
     * @return true：所有；false：特定类型。
     */
    boolean isForAll() {
        return StringUtils.isEmpty(className);
    }

    /**
     * 获取更新内容方法。
     *
     * @return true：刷新内容；false：重新加载内容。
     */
    boolean isRefresh() {
        return refresh;
    }

    /**
     * 获取关闭内容方法。
     *
     * @return true：刷新内容；false：重新加载内容。
     */
    public boolean isClose() {
        return isClose;
    }

    public int getClosePosition() {
        return closePosition;
    }

    public void setClosePosition(int closePosition) {
        this.closePosition = closePosition;
    }
}
