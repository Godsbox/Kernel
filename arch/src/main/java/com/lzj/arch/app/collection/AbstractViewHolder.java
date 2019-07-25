/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.lzj.arch.app.collection.ItemContract.Presenter;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.core.Controller;
import com.lzj.arch.core.PresenterDelegate;
import com.lzj.arch.util.ViewUtils;

import butterknife.ButterKnife;

import static com.lzj.arch.core.Arch.newPresenterDelegate;
import static com.lzj.arch.core.PresenterManager.KEY_PRESENTER_ID;

/**
 * 项视图基类。
 *
 * @author 吴吉林
 */
public abstract class AbstractViewHolder<P extends Presenter>
        extends ViewHolder
        implements Controller<P>, ItemContract.PassiveView {

    /**
     * 路由器。
     */
    private Router router;

    /**
     * 是否是网页
     */
    private boolean isWebView;

    /**
     * 表现者委托。
     */
    private PresenterDelegate<P> presenterDelegate = newPresenterDelegate(this);

    /**
     * 状态。
     */
    private Bundle state = new Bundle(1);

    public AbstractViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onItemClick(getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getPresenter().onItemLongClick(getAdapterPosition());
                return true;
            }
        });
    }

    void create(Router router) {
        this.router = router;
        onFindView();
        onInitView();
    }

    @SuppressWarnings("unchecked")
    void attachPresenter(ItemManager itemManager, ItemModel model, ParentPresenter parentPresenter) {
        state.putString(KEY_PRESENTER_ID, model.getItemId());
        presenterDelegate.onViewCreate(this, state, null, itemManager);
        ItemPresenter itemPresenter = (ItemPresenter) presenterDelegate.getPresenter();
        itemPresenter.setModel(model);
        itemPresenter.setParentPresenter(parentPresenter);
        presenterDelegate.attachView(this, true);
    }

    void detachPresenter() {
        presenterDelegate.getPresenter().onRecycled();
        presenterDelegate.detachView();
        onRecycled();
    }

    /**
     * 处理项视图被回收事件。
     */
    protected void onRecycled() {
    }

    @Override
    public P createPresenter() {
        return presenterDelegate.createPresenter();
    }

    @Override
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    public Router getRouter() {
        return router;
    }

    /**
     * 在查找视图项子视图时调用。
     */
    protected void onFindView() {
        // 空实现
    }

    /**
     * 在初始化视图项子视图调用。
     */
    protected void onInitView() {
        // 空实现
    }

    /**
     * 根据指定的视图 ID 查找视图。这里通过泛型和类型推导机制，省去手写强制转换。
     *
     * @param id  视图 ID
     * @param <V> 视图类型
     * @return 视图
     */
    @SuppressWarnings("unchecked")
    protected <V> V findView(int id) {
        return ViewUtils.findView(itemView, id);
    }

    /**
     * 根据指定的视图 ID 查找父视图的子视图。这里通过泛型和类型推导机制，省去手写强制转换。
     *
     * @param parent 父视图
     * @param id     视图 ID
     * @param <V>    视图类型
     * @return 视图
     */
    @SuppressWarnings("unchecked")
    protected <V> V findView(View parent, int id) {
        return ViewUtils.findView(parent, id);
    }

    /**
     * 获取上下文。
     *
     * @return 上下文
     */
    protected Context getContext() {
        return itemView.getContext();
    }

    public void setWebView(boolean webView) {
        isWebView = webView;
    }

    public boolean isWebView() {
        return isWebView;
    }
}
