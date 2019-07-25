/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import com.lzj.arch.app.content.ContentContract;

import java.util.List;

/**
 * 集合内容契约。
 *
 * @author 吴吉林
 */
public interface CollectionContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ContentContract.PassiveView {

        /**
         * 设置数据集。
         *
         * @param dataSet 数据集
         */
        void setDataSet(List<ItemModel> dataSet);

        /**
         * 设置RecyclerView横向布局管理。
         */
        void setHorizontalLayout();

        /**
         * 通知给定位置的数据项已插入
         *
         * @param position 数据项位置
         */
        void notifyItemInserted(int position);

        /**
         * 通知指定位置上的数据项已删除。
         *
         * @param position 数据项位置
         */
        void notifyItemRemoved(int position);

        /**
         * 通知指定位置上的数据项已删除。
         *
         * @param position 数据项位置
         * @param itemCount 数据项数量
         */
        void notifyItemRangeRemoved(int position, int itemCount);

        /**
         * 通知给定范围的数据项已插入。
         *
         * @param positionStart 范围开始位置
         * @param itemCount 数据项数量
         */
        void notifyItemRangeInserted(int positionStart, int itemCount);

        /**
         * 通知指定位置上的数据项已变化。
         *
         * @param position 数据项位置
         */
        void notifyItemChanged(int position);

        /**
         * 通知给定范围的数据项已变化。
         *
         * @param positionStart 范围开始位置
         * @param itemCount 数据项数量
         */
        void notifyItemRangeChanged(int positionStart, int itemCount);

        /**
         * 通知视图数据集已变化。
         */
        void notifyDataSetChanged();

        /**
         * 平滑滚动到指定位置上的视图的底部。
         *
         * @param position 位置
         */
        void smoothScrollToPosition(int position);

        /**
         * 滚动到指定位置上的视图的顶部。
         *
         * @param position 位置
         */
        void scrollToTop(int position);

        /**
         * 处理加载更多触发事件。
         */
        void onScrollStateChange(int newState);

        /**
         * 设置网格总数。
         *
         * @param spanCount 网格总数
         */
        void setSpanCount(int spanCount);

    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ContentContract.Presenter, ParentPresenter {

        /**
         * 处理加载更多触发事件。
         */
        void onLoadMoreTrigger();

        /**
         * item的点击事件。
         */
        void onItemClick(int position);

        /**
         * item的点击事件。
         */
        void onItemLongClick(int position);

        /**
         * 滑动
         * @param firstVisiblePosition
         * @param lastVisiblePosition
         */
        void onScroll(int firstVisiblePosition, int lastVisiblePosition);

        /**
         * 返回位置的dy值
         */
        void onScroll(int firstVisiblePosition, int lastVisiblePosition, int dy);

        /**
         * 是否已经是顶部
         */
        void isTop(boolean sure);
    }
}
