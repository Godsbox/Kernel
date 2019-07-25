/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.annotation.Nullable;

import com.lzj.arch.app.collection.blank.BlankItemModel;
import com.lzj.arch.app.collection.more.MoreItemModel;
import com.lzj.arch.app.content.ContentModel;
import com.lzj.arch.util.CollectionUtils;

import java.util.List;

/**
 * 集合表现模型。
 *
 * @author 吴吉林
 */
public abstract class CollectionModel<T extends CollectionResult> extends ContentModel<List<ItemModel>> {

    /**
     * 页码。
     */
    private int page = 1;

    /**
     * 结果数据。
     */
    private T result;

    /**
     * 标识是否正在加载更多。
     */
    private boolean moreLoading;

    /**
     * 标识是否还有更多。
     */
    private boolean hasMore;

    /**
     * 网格总数。
     */
    private int spanCount;

    /**
     * 是否启用加载更多功能。
     */
    private boolean loadMoreEnabled = true;

    /**
     * 已加载更多项的数量。
     */
    private int moreLoadedCount;

    /**
     * 每页内容。
     */
    private List<ItemModel> pageContent;

    /**
     * 更多项表现模型。
     */
    private MoreItemModel moreItem = new MoreItemModel();

    private int firstVisiblePosition;

    private int lastVisiblePosition;

    /**
     * 需要显示更多项吗
     */
    private boolean isNeedMore = true;

    void setResult(T result, List<ItemModel> pageContent) {
        this.result = result;
        this.pageContent = pageContent;
    }

    protected T getResult() {
        return result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isMoreLoading() {
        return moreLoading;
    }

    public boolean isLoadMoreEnabled() {
        return loadMoreEnabled;
    }

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        this.loadMoreEnabled = loadMoreEnabled;
    }

    public void setMoreText(int content) {
        getMoreItem().setMoreContent(content);
    }

    public void setMoreBackground(int color) {
        getMoreItem().setMoreBackground(color);
    }

    @Override
    public List<ItemModel> getContent() {
        return super.getContent();
    }

    /**
     * 获取给定位置的项模型。
     *
     * @param position 位置
     * @param <M> 项模型类型
     * @return 项表现模型，或null。
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <M> M getItemModel(int position) {
        try {
            return (M) CollectionUtils.getItem(getContent(), position);
        } catch (Exception e) {
            return null;
        }
    }

    void setMoreLoading(boolean moreLoading) {
        this.moreLoading = moreLoading;
        moreItem.setMoreLoading(moreLoading);
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        moreItem.setHasMore(hasMore);
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    int getMoreLoadedCount() {
        return moreLoadedCount;
    }

    void setMoreLoadedCount(int moreLoadedCount) {
        this.moreLoadedCount = moreLoadedCount;
    }

    /**
     * 判断是否是第一页。
     *
     * @return true：第一页；false：非第一页。
     */
    protected boolean isFirstPage() {
        return page == 1;
    }

    @Override
    public boolean isContentEmpty(List<ItemModel> content) {
        return CollectionUtils.isEmpty(content);
    }

    public MoreItemModel getMoreItem() {
        return moreItem;
    }

    void removeMoreItem() {
        CollectionUtils.removeLast(getContent());
    }

    /**
     * 追加空白项表现模型。
     *
     * @param cellCount 当前单元格个数
     */
    protected void appendBlank(int cellCount) {
        ItemModel item = CollectionUtils.getLast(pageContent);
        if (item == null || item.getSpanSize() == 0) {
            return;
        }
        int spanSize = item.getSpanSize();
        int columnCount = spanCount / spanSize;
        int mod = cellCount % columnCount;
        if (mod == 0) {
            return;
        }
        BlankItemModel blank = new BlankItemModel();
        blank.setSpanSize(spanSize);
        for (int i = 0; i < columnCount - mod; i++) {
            CollectionUtils.add(pageContent, blank);
        }
    }

    public int getFirstVisiblePosition() {
        return firstVisiblePosition;
    }

    public void setFirstVisiblePosition(int firstVisiblePosition) {
        this.firstVisiblePosition = firstVisiblePosition;
    }

    public int getLastVisiblePosition() {
        return lastVisiblePosition;
    }

    public void setLastVisiblePosition(int lastVisiblePosition) {
        this.lastVisiblePosition = lastVisiblePosition;
    }

    /**
     * 将结果数据转化成内容。
     *
     * @param result  结果数据
     * @param content 内容
     */
    protected abstract void map(T result, List<ItemModel> content);

    public boolean isNeedMore() {
        return isNeedMore;
    }

    public void setNeedMore(boolean needMore) {
        isNeedMore = needMore;
    }
}
