/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SimpleItemAnimator;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.CollectionContract.PassiveView;
import com.lzj.arch.app.collection.CollectionContract.Presenter;
import com.lzj.arch.app.collection.blank.BlankItemDelegate;
import com.lzj.arch.app.collection.empty.EmptyItemDelegate;
import com.lzj.arch.app.collection.layoutmanager.SmoothScrollGridLayoutManager;
import com.lzj.arch.app.collection.layoutmanager.SmoothScrollLinearLayoutManager;
import com.lzj.arch.app.collection.more.MoreItemDelegate;
import com.lzj.arch.app.content.ContentFragment;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;

/**
 * 集合内容界面。
 *
 * @author 吴吉林
 */
public abstract class CollectionFragment<P extends Presenter>
        extends ContentFragment<P>
        implements PassiveView {

    /**
     * 向下滚动方向
     */
    private static final int DIRECTION_DOWN = 1;

    /**
     * 适配器。
     */
    private CollectionAdapter adapter = new CollectionAdapter(this);

    /**
     * 集合视图。
     */
    private RecyclerView collectionView;

    /**
     * 布局管理者
     */
    private LayoutManager mManager;

    /**
     * 是否返回滑动的dy值
     */
    private boolean needScrollDistance;

    /**
     * 是否需要滑动状态值
     */
    private boolean needScrollStateChanged;

    /**
     * 是否返回已经到达顶部
     */
    private boolean isTop;

    {
        getConfig().setLayoutResource(R.layout.app_fragment_collection);
        registerItemDelegate(EmptyItemDelegate.class);
        registerItemDelegate(MoreItemDelegate.class);
        registerItemDelegate(BlankItemDelegate.class);
    }

    @CallSuper
    @Override
    public void onFindView() {
        super.onFindView();
        collectionView = findView(R.id.collection);
    }

    @Override
    public void setHorizontalLayout() {
        if (mManager == null) {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mManager = manager;
        }
    }

    @CallSuper
    @Override
    public void onInitView(Bundle state) {
        super.onInitView(state);
        collectionView.addOnScrollListener(new OnScrollListener());
        adapter.setRouter(getRouter());
        adapter.setTag(getConfig().getName());
        ((SimpleItemAnimator) collectionView.getItemAnimator()).setSupportsChangeAnimations(false);
        collectionView.setAdapter(adapter);
    }

    /**
     * 添加滑动监听
     *
     * @param listener 监听器
     */
    protected void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        this.collectionView.addOnScrollListener(listener);
    }

    /**
     * 添加项装饰。
     *
     * @param decoration 项装饰
     */
    protected void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        collectionView.addItemDecoration(decoration);
    }

    @Override
    public void setSpanCount(int spanCount) {
        LayoutManager manager = collectionView.getLayoutManager();
        if (manager == null) {
            if (mManager != null) {
                collectionView.setLayoutManager(mManager);
            } else {
                collectionView.setLayoutManager(createLayoutManager(spanCount));
            }
            return;
        }
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanCount(spanCount);
        }
    }

    /**
     * 创建布局管理器。
     *
     * @param spanCount 网格总数
     * @return 布局管理器
     */
    private LayoutManager createLayoutManager(int spanCount) {
        if (spanCount > 1) {
            final SmoothScrollGridLayoutManager manager = new SmoothScrollGridLayoutManager(getActivity(), spanCount);
            manager.setSpanSizeLookup(new SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.getSpanSize(position, manager.getSpanCount());
                }
            });
            return manager;
        }
        return new SmoothScrollLinearLayoutManager(getActivity());
    }

    /**
     * 注册项代表。
     *
     * @param delegateClass 项代表类型
     */
    protected void registerItemDelegate(Class<? extends ItemDelegate> delegateClass) {
        adapter.registerItemDelegate(delegateClass);
    }

    @Override
    public void setDataSet(List<ItemModel> dataSet) {
        adapter.setList(dataSet);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemRangeRemoved(int position, int itemCount) {
        adapter.notifyItemRangeRemoved(position, itemCount);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyItemChanged(int position) {
        if (collectionView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (collectionView.isComputingLayout() == false)) {
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyDataSetChanged() {
        if (collectionView != null) {
            if (collectionView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (collectionView.isComputingLayout() == false)) {
                adapter.notifyDataSetChanged();
                /*if(needLastPosition && lastPosition > 1){
                    scrollToPosition();
                }*/
            }
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (collectionView != null && position >= 0) {
            collectionView.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onScrollStateChange(int newState) {
        //空实现
    }

    @Override
    public void scrollToTop(int position) {
        LayoutManager manager = collectionView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) manager).scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.removeAllItemPresenters();
    }

    public void setNeedScrollDistance(boolean needScrollDistance) {
        this.needScrollDistance = needScrollDistance;
    }

    public void setNeedStatusIsTop(boolean need) {
        this.isTop = need;
    }

    public void setNeedScrollStateChanged(boolean needScrollStateChanged) {
        this.needScrollStateChanged = needScrollStateChanged;
    }

    /**
     * 滚动监听器。
     */
    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(needScrollStateChanged){
                onScrollStateChange(newState);
            }
            if (newState == SCROLL_STATE_DRAGGING) {
                return;
            }
            /*if(needLastPosition){
                getPositionAndOffset();
            }*/
            boolean canScrollDown = collectionView.canScrollVertically(DIRECTION_DOWN);
            if (canScrollDown) {
                return;
            }
            getPresenter().onLoadMoreTrigger();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LayoutManager manager = collectionView.getLayoutManager();
            if (!(manager instanceof LinearLayoutManager)) {
                return;
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            getPresenter().onScroll(firstVisiblePosition, lastVisiblePosition);

            if (isTop) {
                getPresenter().isTop(!recyclerView.canScrollVertically(-1));
            }
            if (needScrollDistance) {
                getPresenter().onScroll(firstVisiblePosition, lastVisiblePosition, dy);
            }
        }
    }
}
