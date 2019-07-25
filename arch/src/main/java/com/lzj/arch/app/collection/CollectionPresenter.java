/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;

import com.lzj.arch.app.collection.CollectionContract.PassiveView;
import com.lzj.arch.app.collection.CollectionContract.Presenter;
import com.lzj.arch.app.collection.more.MoreItemModel;
import com.lzj.arch.app.content.ContentPresenter;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.core.State;

import static com.lzj.arch.app.content.ContentModel.TYPE_LOAD_MORE;
import static com.lzj.arch.util.CollectionUtils.getSize;

/**
 * 集合内容表现者。
 *
 * @author 吴吉林
 */
public abstract class CollectionPresenter
        <V extends PassiveView, M extends CollectionModel, Rr extends Router>
        extends ContentPresenter<V, M, Rr>
        implements Presenter {

    /**
     * 当前项表现者。
     */
    private ItemPresenter currentPresenter;

    {
        addState(new MoreItemChanged());
        addState(new LoadMoreSuccess());
    }

    @Override
    protected void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        if (newView) {
            getView().setSpanCount(getModel().getSpanCount());
        }
        super.onViewAttach(reattach, newView, isVisibleToUser);
    }

    @Override
    public void onScroll(int firstVisiblePosition, int lastVisiblePosition) {
        getModel().setFirstVisiblePosition(firstVisiblePosition);
        getModel().setLastVisiblePosition(lastVisiblePosition);
    }

    @Override
    public void onScroll(int firstVisiblePosition, int lastVisiblePosition, int dy) {
        getModel().setFirstVisiblePosition(firstVisiblePosition);
        getModel().setLastVisiblePosition(lastVisiblePosition);
    }

    @Override
    public void isTop(boolean sure) {
        // 空实现
    }

    @Override
    public void onLoadMoreTrigger() {
        if (!getModel().isLoadMoreEnabled()
                || getModel().isMoreLoading()
                || !getModel().hasMore()) {
            return;
        }
        getModel().setType(TYPE_LOAD_MORE);
        getModel().setMoreLoading(true);
        changeMoreItemModel(true, "");
        int nextPage = getModel().getPage() + 1;
        getModel().setPage(nextPage);
        doLoadMore(nextPage);
    }

    @Override
    public void onItemClick(int position) {
        // 空实现
    }

    @Override
    public void onItemLongClick(int position) {
        // 空实现
    }

    @Override
    public void setCurrentPresenter(boolean clean, ItemPresenter presenter) {
        // 当前项表现者不再是presenter则不需要清除。
        if (clean && currentPresenter != presenter) {
            return;
        }
        // 当前项表现者已经是presenter则不需要再次设置。
        if (!clean && currentPresenter == presenter) {
            return;
        }
        if (currentPresenter != null) {
            currentPresenter.onPause();
            currentPresenter = null;
        }
        if (!clean && presenter != null) {
            presenter.onResume();
            currentPresenter = presenter;
        }
    }

    @Override
    public void setAppbarColor(@ColorInt int color) {
        getView().setToolbarColor(color);
    }

    @Override
    public void setAppbarVisible(int visible) {
        getView().setAppbarVisible(visible);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPresenter != null) {
            currentPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentPresenter != null) {
            currentPresenter.onPause();
        }
    }

    @Override
    @CallSuper
    @SuppressWarnings("unchecked")
    protected void onContentLoaded() {
        getView().setDataSet(getModel().getContent());
        getView().notifyDataSetChanged();
    }

    @Override
    protected final void load() {
        getModel().setPage(1);
        super.load();
    }

    @Override
    public void onRefreshTrigger() {
        getModel().setPage(1);
        super.onRefreshTrigger();
    }

    @Override
    protected void doLoad() {
        doLoadMore(1);
    }

    /**
     * 处理加载更多内容。
     *
     * @param page 页码
     */
    protected void doLoadMore(int page) {

    }

    /**
     * 处理刷新更多内容。
     */
    protected void doRefreshMore() {
    }

    /**
     * 遍历所有项模型，并处理特定类型的项模型。
     *
     * @param itemClass 项模型类型
     * @param action 遍历操作
     * @param <T> 项模型类型
     */
    @SuppressWarnings("unchecked")
    public <T extends ItemModel> void forEach(Class<T> itemClass, ForEachAction<T> action) {
        M model = getModel();
        for (int i = 0; i < getSize(model.getContent()); i++) {
            Object item = model.getItemModel(i);
            if (!itemClass.isInstance(item)) {
                continue;
            }
            boolean needBreak = action.accept(i, (T) item);
            if (needBreak) {
                break;
            }
        }
    }

    /**
     * 加载更多项已变化状态。
     */
    private class MoreItemChanged implements State<PassiveView, CollectionModel> {
        @Override
        public void render(PassiveView view, CollectionModel model) {
            if (!model.isLoadMoreEnabled()) {
                return;
            }
            int position = getSize(getModel().getContent()) - 1;
            getView().notifyItemChanged(position);
        }
    }

    /**
     * 加载更多成功状态。
     */
    class LoadMoreSuccess implements State<PassiveView, CollectionModel> {
        @Override
        public void render(PassiveView view, CollectionModel model) {
            getView().notifyItemRangeChanged(model.getContent().size(), model.getMoreLoadedCount());
        }
    }

    /**
     * 改变更多项表现模型。
     *
     * @param hasMore 是否还有更多
     * @param message 提示消息
     */
    void changeMoreItemModel(boolean hasMore, String message) {
        MoreItemModel itemModel = getModel().getMoreItem();
        itemModel.setHasMore(hasMore);
        itemModel.setMessage(message);
        renderState(MoreItemChanged.class);
    }

    protected void setTitle(String title) {
        getView().setTitle(title);
    }

}
