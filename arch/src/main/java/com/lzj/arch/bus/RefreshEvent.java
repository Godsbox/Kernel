/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

/**
 * 更新事件。
 *
 * @author wsy
 */
public class RefreshEvent {

    /**
     *  作品详情页 评论页面刷新
     */
    public static final int GAME_DETAIL_COMMENT_PAGE = 1;

    /**
     *  作品详情页 所有页面刷新
     */
    public static final int GAME_DETAIL_ALL_PAGE = 2;

    private int type;

    public RefreshEvent(){

    }

    public RefreshEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
