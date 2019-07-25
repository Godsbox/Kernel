/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content.state;

import com.lzj.arch.app.content.ContentContract.PassiveView;
import com.lzj.arch.app.content.ContentModel;
import com.lzj.arch.core.State;

import static com.lzj.arch.rx.ObservableException.ERROR_CODE_SECONDARY_EMPTY;

/**
 * 加载无内容状态。
 *
 * @author 吴吉林
 */
public class LoadEmpty implements State<PassiveView, ContentModel> {
    @Override
    public void render(PassiveView view, ContentModel model) {
        boolean secondaryEmpty = model.getErrorCode() == ERROR_CODE_SECONDARY_EMPTY;
        if (!secondaryEmpty) {
            view.showLoadEmpty();
            return;
        }
        view.showLoadEmptySecondary(model.getErrorMessage());
    }
}
