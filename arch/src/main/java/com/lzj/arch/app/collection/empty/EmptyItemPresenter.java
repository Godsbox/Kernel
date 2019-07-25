package com.lzj.arch.app.collection.empty;

import com.lzj.arch.app.collection.ItemPresenter;
import com.lzj.arch.app.collection.empty.EmptyItemContract.PassiveView;
import com.lzj.arch.app.collection.empty.EmptyItemContract.Presenter;
import com.lzj.arch.bus.BusManager;
import com.lzj.arch.bus.EmptyActionEvent;
import com.lzj.arch.bus.RefreshEvent;
import com.lzj.arch.core.Contract;

/**
 * 空项表现者。
 */
public class EmptyItemPresenter extends ItemPresenter<PassiveView, EmptyItemModel, Contract.Router>
        implements Presenter {

    @Override
    protected void onBind() {
        getView().showEmpty(getModel().getImageResId(),
                getModel().getMessage(),
                getModel().getMessageResId(),
                getModel().getSecondMessage(),
                getModel().getSecondMessageResId(),
                getModel().getAction(),
                getModel().getActionResId(),
                getModel().getBackgroundColorId());
    }

    @Override
    public void onReload() {
        BusManager.postResponse(new RefreshEvent(RefreshEvent.GAME_DETAIL_COMMENT_PAGE));
    }

    @Override
    public void onActionClicked() {
        //发送点击事件，带上标识
        BusManager.postUi(new EmptyActionEvent(getModel().getActionEvent()));
    }
}
