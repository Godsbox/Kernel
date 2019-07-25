/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 帧布局实现的加载容器视图。
 *
 * @author 吴吉林
 */
public class FrameLayoutContainer extends FrameLayout implements LoadViewContainer {

    /**
     * 内容加载视图显示位置。
     */
    private int loadViewIndex;

    public FrameLayoutContainer(@NonNull Context context) {
        this(context, null, 0);
    }

    public FrameLayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadViewIndex = ContentLoadView.getLoadViewIndex(context, attrs, -1);
    }

    @Override
    public int getLoadViewIndex() {
        return loadViewIndex;
    }
}
