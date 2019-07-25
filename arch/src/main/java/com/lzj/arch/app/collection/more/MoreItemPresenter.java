/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.more;

import com.lzj.arch.app.collection.ItemPresenter;
import com.lzj.arch.app.collection.more.MoreItemContract.PassiveView;
import com.lzj.arch.app.collection.more.MoreItemContract.Presenter;
import com.lzj.arch.core.Contract;
import com.lzj.arch.util.ContextUtils;

/**
 * 更多项表现者。
 *
 * @author 吴吉林
 */
public class MoreItemPresenter
        extends ItemPresenter<PassiveView, MoreItemModel, Contract.Router>
        implements Presenter {

    @Override
    protected void onBind() {
        // 正在加载更多
        if (getModel().isMoreLoading()) {
            getView().setMessage(false, "");
            getView().setProgressVisible(true);
            getView().setNoMoreVisible(false);
            return;
        }

        getView().setMoreBackground(getModel().getMoreBackground());
        getView().setMoreBackgroundImg(getModel().getMoreBackgroundImg(),
                getModel().getMoreBackgroundImgText(),
                getModel().getMoreBackgroundImgIcon(),
                getModel().getMoreBackgroundImgTextColor());

        // 没有更多
        getView().setProgressVisible(false);
        if (!getModel().hasMore()) {
            getView().setNoMoreVisible(true);
            getView().setNoMoreText(getModel().getNoMoreText());
            getView().setMessage(false, "");
            getView().setMoreText(getModel().getMoreContent());
            return;
        }

        // 显示提示消息
        getView().setNoMoreVisible(false);
        if (getModel().getMessageId() > 0) {
            getView().setMessage(true, getModel().getMessageId());
            return;
        }
        getView().setMessage(true, getModel().getMessage());
    }

    @Override
    public void onItemClick(int position) {
        if (getModel().getMoreActionIntent() != null) {
            ContextUtils.getAppContext().startActivity(getModel().getMoreActionIntent());
        }
    }
}
