/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;


/**
 * 下载基本事件。
 *
 * @author 王盛钰
 */
public class DownloadUpdateEvent {


    public static final int DOWNLOADLIST_COMPLETE_UPDATE = 9;

    private int gameId;

    private int type;

    public DownloadUpdateEvent(int type,int gameId){
        this.type = type;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }


    public int getType() {
        return type;
    }
}
