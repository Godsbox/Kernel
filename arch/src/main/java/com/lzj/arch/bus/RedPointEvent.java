/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;

/**
 * 小红点有关事件
 * @author wsy
 * @time 2018/6/19 9:40
 */

public class RedPointEvent {

    /**
     *  显示还是隐藏
     */
    private boolean visible;

    /**
     * 数量
     */
    private int count;

    public RedPointEvent(){}

    public RedPointEvent(boolean visible){
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
