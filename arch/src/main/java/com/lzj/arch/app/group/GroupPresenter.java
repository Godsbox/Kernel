/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.group;

import com.lzj.arch.app.content.ContentPresenter;
import com.lzj.arch.app.group.GroupContract.PassiveView;
import com.lzj.arch.app.group.GroupContract.Presenter;
import com.lzj.arch.core.Contract.Router;

/**
 * 分组内容表现者。
 *
 * @author 吴吉林
 */
public abstract class GroupPresenter<V extends PassiveView, M extends GroupModel, R extends Router>
        extends ContentPresenter<V, M, R>
        implements Presenter {

    {
        getModel().setShowLoading(false);
    }

    @Override
    public void onPageChange(int position) {
        // 空实现
    }

    @Override
    public void onTabReselected(int position) {
        // 空实现
    }
}
