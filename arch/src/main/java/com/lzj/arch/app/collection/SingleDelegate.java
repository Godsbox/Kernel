package com.lzj.arch.app.collection;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 单布局代理
 */
public abstract class SingleDelegate implements ItemDelegate {

    @LayoutRes
    public abstract long getLayoutId();

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
        if (itemType == getLayoutId()) {
            return true;
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
