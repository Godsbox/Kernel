/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项代表抽象实现类。
 *
 * @author 吴吉林
 */
public abstract class AbstractItemDelegate implements ItemDelegate {

    /**
     * 所能代表的布局资源 ID。
     */
    private SparseIntArray layoutIds = new SparseIntArray(3);

    /**
     * 添加布局资源 ID。
     *
     * @param layoutId 布局资源 ID
     */
    protected void addLayoutId(int layoutId) {
        layoutIds.put(layoutId, layoutId);
    }

    @Override
    public final ViewHolder createViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(itemType, parent, false);
        ViewHolder holder = createViewHolder(itemView, itemType);
        if (holder instanceof AbstractViewHolder) {
            return holder;
        }
        throw new IllegalArgumentException("视图持有者必须是 AbstractViewHolder 的子类");
    }

    @Override
    public final boolean matches(int itemType) {
        for (int i = 0; i < layoutIds.size(); i++) {
            if (itemType == layoutIds.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建视图持有者。
     *
     * @param itemView 项视图
     * @param itemType 项类型
     * @return 视图持有者
     */
    protected abstract ViewHolder createViewHolder(View itemView, int itemType);
}
