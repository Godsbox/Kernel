/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import com.lzj.arch.app.content.ContentContract.PassiveView;
import com.lzj.arch.app.content.state.LoadEmpty;
import com.lzj.arch.app.content.state.LoadFailure;
import com.lzj.arch.app.content.state.RefreshEmpty;
import com.lzj.arch.app.content.state.RefreshFailure;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.rx.ObservableException;
import com.lzj.arch.rx.ObserverAdapter;

import static com.lzj.arch.app.content.ContentModel.TYPE_LOAD;
import static com.lzj.arch.app.content.ContentModel.TYPE_REFRESH;
import static com.lzj.arch.rx.ObservableException.ERROR_CODE_NONE;
import static com.lzj.arch.rx.ObservableException.ERROR_CODE_SECONDARY_EMPTY;

/**
 * 内容观察者。
 *
 * @author 吴吉林
 */
public class ContentObserver<T> extends ObserverAdapter<T> {

    /**
     * 内容表现者。
     */
    private ContentPresenter<? extends PassiveView, ? extends ContentModel<T>, ? extends Router> presenter;

    public ContentObserver(ContentPresenter<? extends PassiveView, ? extends ContentModel<T>, ? extends Router> presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onNext(T content) {
        // 如果是已设置的 特殊错误 则不重置
        if(presenter.getModel().getErrorCode() != ERROR_CODE_SECONDARY_EMPTY){
            presenter.getModel().setError(ERROR_CODE_NONE, "");
        }
        if (presenter.getModel().isContentEmpty(content)) {
            onEmpty();
            return;
        }
        onSuccess(content);
    }

    private void onSuccess(T content) {
        presenter.getModel().setContent(content);
        switch (presenter.getModel().getType()) {
            case TYPE_LOAD:
            case TYPE_REFRESH:
                presenter.renderState(ContentPresenter.LoadSuccessState.class);
                break;
            default:
                break;
        }
    }

    private void onEmpty() {
        switch (presenter.getModel().getType()) {
            case TYPE_LOAD:
                presenter.renderState(LoadEmpty.class);
                presenter.onContentUnloaded(true);
                break;
            case TYPE_REFRESH:
                presenter.renderState(RefreshEmpty.class);
                break;
        }
    }

    @Override
    public void onError(ObservableException e) {
        presenter.getModel().setError(e.getErrorCode(), e.getMessage());
        switch (presenter.getModel().getType()) {
            case TYPE_LOAD:
                // 给个后门，当出现一个为空错误时显示空页面，而不是失败页面。
                boolean secondaryEmpty = e.getErrorCode() == ERROR_CODE_SECONDARY_EMPTY;
                presenter.onContentUnloaded(secondaryEmpty);
                if (secondaryEmpty) {
                    presenter.renderState(LoadEmpty.class);
                    return;
                }
                presenter.renderState(LoadFailure.class);
                break;
            case TYPE_REFRESH:
                presenter.renderState(RefreshFailure.class);
                break;
        }
    }
}
