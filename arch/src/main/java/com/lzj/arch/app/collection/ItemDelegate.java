/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

/**
 * 项代表接口。
 *
 * @author 吴吉林
 */
public interface ItemDelegate {

    /**
     * 创建视图持有者。
     *
     * @param parent 父视图
     * @param itemType 项类型
     * @return 视图持有者
     */
    ViewHolder createViewHolder(ViewGroup parent, int itemType);

    /**
     * 判断该项代表是否与给定的项类型相匹配。
     *
     * @param itemType 项类型
     * @return true：匹配；false：不匹配。
     */
    boolean matches(int itemType);
}
