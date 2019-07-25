/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import com.lzj.arch.core.Model;

import static java.util.UUID.randomUUID;

/**
 * 项表现模型。
 *
 * @author 吴吉林
 */
public abstract class ItemModel extends Model {

    /**
     * 项类型。
     */
    private int itemType;

    /**
     * 项 ID。
     */
    private String itemId = randomUUID().toString();

    /**
     * 占据网格大小。
     */
    private int spanSize;

    /**
     * 获取项 ID。
     *
     * @return 项 ID
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * 获取占据网格大小。
     *
     * @return 占据网格大小
     */
    public int getSpanSize() {
        return spanSize;
    }

    /**
     * 设置占据网格大小。
     *
     * @param size 占据网格大小
     */
    public void setSpanSize(int size) {
        spanSize = size;
    }

    /**
     * 设置项类型。
     *
     * @param type 项类型
     */
    public void setItemType(int type) {
        itemType = type;
    }

    /**
     * 获取项类型。
     *
     * @return 项类型
     */
    public int getItemType() {
        return itemType;
    }
}
