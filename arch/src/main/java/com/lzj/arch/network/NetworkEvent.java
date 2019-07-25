/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

/**
 * 网络事件。
 *
 * @author 吴吉林
 */
public final class NetworkEvent {

    /**
     * 网络事件类型：无网络、手机网络、WiFi 网络。
     */
    private final int type;

    /**
     * 事件名称。
     */
    private final String name;

    /**
     * 网络类型：无网络。
     */
    private static final int TYPE_NONE = 1;

    /**
     * 网络类型：手机网络。
     */
    private static final int TYPE_MOBILE = 2;

    /**
     * 网络类型：WiFi 网络。
     */
    private static final int TYPE_WIFI = 3;

    /**
     * 无网络事件。
     */
    private static final NetworkEvent NONE = new NetworkEvent(TYPE_NONE, "NONE");

    /**
     * WiFi 网络事件。
     */
    private static final NetworkEvent WIFI = new NetworkEvent(TYPE_WIFI, "WIFI");

    /**
     * 手机网络事件。
     */
    private static final NetworkEvent MOBILE = new NetworkEvent(TYPE_MOBILE, "MOBILE");

    private NetworkEvent(int type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 获取一个新的无网络事件。
     *
     * @return 新的无网络事件
     */
    public static NetworkEvent getNone() {
        return NONE;
    }

    /**
     * 获取一个新的手机网络事件。
     *
     * @return 新的手机网络事件
     */
    public static NetworkEvent getMobile() {
        return MOBILE;
    }

    /**
     * 获取一个新的 WiFi 网络事件。
     *
     * @return 新的 WiFi 网络事件
     */
    public static NetworkEvent getWifi() {
        return WIFI;
    }

    /**
     * 判断是否为无网络事件。
     *
     * @return true：是无网络事件，false：不是无网络事件
     */
    public boolean isNone() {
        return type == TYPE_NONE;
    }

    /**
     * 判断是否为手机网络事件。
     *
     * @return true：是手机网络事件，false：不是手机网络事件
     */
    public boolean isMobile() {
        return type == TYPE_MOBILE;
    }

    /**
     * 判断是否为 WiFi 网络事件。
     *
     * @return true：是 WiFi 网络事件，false：不是 WiFi 网络事件
     */
    public boolean isWifi() {
        return type == TYPE_WIFI;
    }

    /**
     * 判断网络是否可用。
     *
     * @return true：可用；false：不可用。
     */
    public boolean isOnline() {
        return !isNone();
    }

    /**
     * 获取事件名称。
     *
     * @return 事件名称
     */
    public String getName() {
        return name;
    }
}
