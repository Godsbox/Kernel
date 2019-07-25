/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import com.lzj.arch.app.PassivePresenter;
import com.lzj.arch.app.collection.ItemContract.PassiveView;
import com.lzj.arch.app.collection.ItemContract.Presenter;
import com.lzj.arch.core.Contract.Router;

/**
 * 项表现者。
 *
 * @param <V> 项视图接口类型
 * @param <M> 项表现模型类型
 * @param <R> 路由器类型
 *
 * @author 吴吉林
 */
public abstract class ItemPresenter
        <V extends PassiveView, M extends ItemModel, R extends Router>
        extends PassivePresenter<V, M, R>
        implements Presenter {

    /**
     * 父表现者。
     */
    private ParentPresenter parentPresenter;

    /**
     * 设置父表现者。
     *
     * @param parentPresenter 父表现者
     */
    void setParentPresenter(ParentPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    protected final void onViewAttach(boolean reattach, boolean newView, boolean isVisibleToUser) {
        super.onViewAttach(reattach, newView, isVisibleToUser);
        onBind();
    }

    @Override
    protected void onViewDetach() {
        super.onViewDetach();
        this.parentPresenter = null;
    }

    /**
     * 滚动到指定位置的顶部。
     *
     * @param position 位置
     */
    protected void scrollToTop(int position) {
        if (parentPresenter != null) {
            parentPresenter.getView().scrollToTop(position);
        }
    }

    /**
     * 作为当前项表现者。
     */
    protected void asCurrentPresenter() {
        if (parentPresenter != null) {
            parentPresenter.setCurrentPresenter(false, this);
        }
    }

    /**
     * 放弃作为当前项表现者。
     */
    protected void dropCurrentPresenter() {
        if (parentPresenter != null) {
            parentPresenter.setCurrentPresenter(true, this);
        }
    }

    /**
     * 获取父表现者。
     *
     * @return 父表现者
     */
    public ParentPresenter getParentPresenter() {
        return parentPresenter;
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
    protected void createModel() {
        // 舍弃父类实现
    }

    @Override
    public void onRecycled() {
        // 空实现
    }

    /**
     * 处理视图已绑定事件。
     */
    protected void onBind() {
        // 空实现
    }
}
