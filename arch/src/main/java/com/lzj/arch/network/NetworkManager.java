/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.lzj.arch.bus.BaseMessageEvent;
import com.lzj.arch.bus.BusManager;
import com.lzj.arch.bus.DownloadEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static com.lzj.arch.util.ContextUtils.getConnectivityManager;
import static com.lzj.arch.util.ContextUtils.getSystemService;
import static com.lzj.arch.util.ContextUtils.getWifiConnectivityManager;

/**
 * 网络管理者。
 *
 * @author 吴吉林
 */
public class NetworkManager {

    /**
     * 最近一次的网络事件。
     */
    public static NetworkEvent currentNetwork;

    /**
     * 检查网络是否可用，可指定是否发布网络事件。
     *
     * @param post 是否发布网络事件
     */
    public static boolean checkConnectivity(boolean post) {
        NetworkEvent event = getNetworkEvent(); // 当前网络事件
        boolean online = !event.isNone();
        // 阻止一次网络变化却多次广播。
        boolean changed = currentNetwork != event;
        if (post) {
            BusManager.postUiSticky(event);
        }
        if (changed && online) {
            BusManager.postResponse(new BaseMessageEvent(BaseMessageEvent.DOWNLAOD_TAB_UPDATE));
        }
        if ((changed && !isWifi()) || event.isNone()) {
            BusManager.postUi(new DownloadEvent(DownloadEvent.PAUSE_PENDING));
        }
        if (isWifi()) {
            BusManager.postResponse(new DownloadEvent(DownloadEvent.WIFI_COME));
        }
        // 记住最近发生的网络变化
        currentNetwork = event;
        return online;
    }

    /**
     * 判断网络是否已连接。
     *
     * @return true：已连接；false：未连接。
     */
    public static boolean isConnected() {
        NetworkInfo info = getConnectivityManager().getActiveNetworkInfo();
        return info != null && info.isConnected() && info.isAvailable();
    }

    /**
     * 判断是否是移动网络。
     *
     * @return true：移动网络；false：WiFi网络或无网络。
     */
    public static boolean isMobile() {
        NetworkInfo info = getConnectivityManager().getActiveNetworkInfo();
        return info != null && info.getType() == TYPE_MOBILE;
    }

    /**
     * 判断是否是WIFI网络。
     *
     * @return true：移动网络；false：WiFi网络或无网络。
     */
    public static boolean isWifi() {
        NetworkInfo info = getConnectivityManager().getActiveNetworkInfo();
        return info != null && info.getType() == TYPE_WIFI;
    }

    /**
     * 获取当前网络事件。
     *
     * @return 网络事件
     */
    public static NetworkEvent getNetworkEvent() {
        ConnectivityManager manager = getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            int type = info.getType();
            switch (type) {
                case TYPE_MOBILE:
                    return NetworkEvent.getMobile();
                case TYPE_WIFI:
                    return NetworkEvent.getWifi();
            }
        }
        return NetworkEvent.getNone();
    }

    /**
     * 获取本机ip地址
     * @return
     */
    public static String getIPAddress() {
        NetworkInfo info = getConnectivityManager().getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = getWifiConnectivityManager();
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 获取本机的dns
     * @return
     */
    public static String getLocalDNS() {
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
