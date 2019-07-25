/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.CollectionContract.PassiveView;
import com.lzj.arch.app.collection.empty.EmptyItemModel;
import com.lzj.arch.app.content.ContentObserver;
import com.lzj.arch.app.content.state.LoadEmpty;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.network.NetworkManager;
import com.lzj.arch.rx.ObservableException;
import com.lzj.arch.rx.ObserverAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.lzj.arch.app.content.ContentModel.TYPE_LOAD;
import static com.lzj.arch.app.content.ContentModel.TYPE_LOAD_MORE;
import static com.lzj.arch.app.content.ContentModel.TYPE_REFRESH;

/**
 * 集合内容观察者。
 *
 * @author 吴吉林
 */
public class CollectionObserver<T extends CollectionResult> extends ObserverAdapter<T> {

    /**
     * 内容观察者。
     */
    private ContentObserver<List<ItemModel>> contentObserver;

    /**
     * 表现者。
     */
    private CollectionPresenter<? extends PassiveView, ? extends CollectionModel<T>, ? extends Router> presenter;

    /**
     * 表现模型。
     */
    private CollectionModel<T> model;

    /**
     * 数据变化时 需要滑到的位置
     */
    private int scrollPosition;

    /**
     * 正常的滚动，直接一步到位
     */
    private boolean scrollNormal;

    /**
     * 用model模式展示加载错误提示
     */
    private boolean showErrorModel;

    public CollectionObserver(CollectionPresenter<? extends PassiveView, ? extends CollectionModel<T>, ? extends Router> presenter) {
        contentObserver = new ContentObserver<>(presenter);
        this.presenter = presenter;
        model = presenter.getModel();
    }

    @Override
    public void onStart() {
        model.getMoreItem().setMessageId(R.string.pull_up_to_load_more);
    }

    @Override
    public void onNext(T data) {
        List<ItemModel> content = new ArrayList<>();
        // 错误到空数据 时会读取错误message 强制重置错误码
        model.setError(0,"");

        model.setResult(data, content);
        model.map(data, content);
        if (model.isContentEmpty(content)) {
            onEmpty(content);
            return;
        }
        model.setHasMore(data.hasMore());
        if (model.isLoadMoreEnabled() && model.isNeedMore()) {
            content.add(model.getMoreItem());
        }
        onSuccess(content);
    }

    private void onSuccess(List<ItemModel> content) {
        switch (model.getType()) {
            case TYPE_LOAD_MORE:
                model.removeMoreItem();
                model.getContent().addAll(content);
                model.setMoreLoadedCount(content.size());
                model.setMoreLoading(false);
                presenter.renderState(CollectionPresenter.LoadMoreSuccess.class);
                break;
            default:
                contentObserver.onNext(content);
                if (model.isLoadContentedScrollTop()) {
                    if(scrollNormal){
                        presenter.getView().scrollToTop(scrollPosition);
                    } else {
                        presenter.getView().smoothScrollToPosition(scrollPosition);
                    }
                }
                // 如果加载到的内容是来自缓存，则需要发起一次刷新内容请求。
                if (model.getResult() != null && model.getType() == TYPE_LOAD && model.isRefreshEnabled()
                        && model.getResult().isFromCache() && NetworkManager.isConnected()) {
                    presenter.refresh();
                }
                break;
        }
    }

    private void onEmpty(List<ItemModel> content) {
        // 如果加载到的内容是来自缓存，则需要发起一次刷新内容请求。
        if (model.getType() == TYPE_LOAD && model.getResult() != null
                && model.getResult().isFromCache() && NetworkManager.isConnected()) {
            presenter.refresh();
        }
        switch (model.getType()) {
            case TYPE_LOAD_MORE:
                model.setMoreLoading(false);
                model.setHasMore(false);
                presenter.changeMoreItemModel(false, "");
                break;
            case TYPE_REFRESH:
                presenter.getModel().setContent(content);
                presenter.onContentLoaded();
                presenter.renderState(LoadEmpty.class);
            default:
                contentObserver.onNext(null);
                break;
        }
    }

    @Override
    public void onError(ObservableException e) {
        switch (model.getType()) {
            case TYPE_LOAD_MORE:
                model.setPage(model.getPage() - 1);
                model.setMoreLoading(false);
                model.getMoreItem().setMessageId(0);
                presenter.changeMoreItemModel(true, e.getMessage());
                break;
            default:
                if(showErrorModel){
                    getNetWorkErrorModel(e.getMessage());
                    return;
                }
                contentObserver.onError(e);
                break;
        }
    }

    private void getNetWorkErrorModel(String message){
        List<ItemModel> content = new ArrayList<>();
        model.setHasMore(false);
        EmptyItemModel emptyItemModel = new EmptyItemModel();
        emptyItemModel.setImage(R.mipmap.app_img_no_network);
        emptyItemModel.setTip(message);
        emptyItemModel.setItemType(R.layout.app_item_network_empty);
        content.add(emptyItemModel);
        onSuccess(content);
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public void setShowErrorModel(boolean showErrorModel) {
        this.showErrorModel = showErrorModel;
    }

    public void setScrollNormal(boolean scrollNormal) {
        this.scrollNormal = scrollNormal;
    }
}
