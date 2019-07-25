/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.util.CollectionUtils;

import java.util.List;

import static com.lzj.arch.util.CollectionUtils.getSize;

/**
 * 集合适配器。
 *
 * @author 吴吉林
 */
public class CollectionAdapter extends Adapter<ViewHolder> {

    /**
     * 项类型：未知。
     */
    public static final int ITEM_TYPE_UNKNOWN = 0;

    /**
     * 列表。
     */
    private List<ItemModel> list;

    /**
     * 路由器。
     */
    private Router router;

    /**
     * 项管理者。
     */
    private ItemManager itemManager = new ItemManager();

    /**
     * 集合界面。
     */
    private CollectionFragment fragment;

    /**
     * 当前界面标签
     */
    private String TAG = "collection_fragment";

    CollectionAdapter(CollectionFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 设置列表。
     *
     * @param list 列表
     */
    public void setList(List<ItemModel> list) {
        this.list = list;
        removeAllItemPresenters();
    }

    @Override
    public int getItemCount() {
        return getSize(list);
    }

    @Override
    public int getItemViewType(int position) {
        ItemModel item = getItem(position);
        return item == null
                ? ITEM_TYPE_UNKNOWN
                : item.getItemType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDelegate delegate = itemManager.getItemDelegate(viewType);
        if (delegate == null) {
            throw new IllegalArgumentException(TAG + "匹配不到视图项代表");
        }
        AbstractViewHolder holder = (AbstractViewHolder) delegate.createViewHolder(parent, viewType);
        holder.create(router);
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        if(viewHolder.isWebView()){
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }

        ItemModel item = getItem(position);
        viewHolder.attachPresenter(itemManager, item, (CollectionPresenter) fragment.getPresenter());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        viewHolder.detachPresenter();
    }

    /**
     * 获取指定位置的数据项。
     *
     * @param position 项位置
     * @return 数据项
     */
    public ItemModel getItem(int position) {
        return CollectionUtils.getItem(list, position);
    }

    /**
     * 注册项代表。
     *
     * @param delegateClass 项代表类型
     */
    void registerItemDelegate(Class<? extends ItemDelegate> delegateClass) {
        itemManager.registerItemDelegate(delegateClass);
    }

    /**
     * 删除所有的项表现者。
     */
    void removeAllItemPresenters() {
        itemManager.removeAll();
    }

    /**
     * 设置路由器。
     *
     * @param router 路由器
     */
    void setRouter(Router router) {
        this.router = router;
    }

    /**
     * 获取给定位置上的数据项的占据网格大小。
     *
     * @param position 位置
     * @param spanCount 网格总数
     * @return 占据网格大小
     */
    int getSpanSize(int position, int spanCount) {
        ItemModel item = getItem(position);
        int spanSize = item.getSpanSize();
        return spanSize == 0 ? spanCount : spanSize;
    }

    public void setTag(String sign) {
        this.TAG = sign;
    }
}
