/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;


/**
 * 下载基本事件。
 *
 * @author 王盛钰
 */
public class DownloadEvent {

    /**
     * 从wifi到4g。
     */
    public static final int PAUSE_PENDING = 10;

    /**
     * 有wifi。
     */
    public static final int WIFI_COME = 1;

    /**
     * 更新播放器通知
     */
    public static final int UPDATE_PLAYER = 9;

    private int type;



    public DownloadEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }


}
