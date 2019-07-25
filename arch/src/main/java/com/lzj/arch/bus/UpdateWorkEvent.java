/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;


/**
 * 基本事件。
 *
 * @author 王盛钰
 */
public class UpdateWorkEvent {


    /**
     * 是否刷新游戏下载列表页。
     */
    public static final int USER_UPDATE_WORK = 99;

    private int gameId;

    private int type;

    private String name;

    public UpdateWorkEvent(int type, int gameId, String name) {
        this.type = type;
        this.name = name;
        this.gameId = gameId;
    }

    public int getType() {
        return type;
    }

    public int getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}
