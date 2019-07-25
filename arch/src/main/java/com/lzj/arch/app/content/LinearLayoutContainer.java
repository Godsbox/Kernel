/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 线性布局实现的加载容器视图。
 *
 * @author 吴吉林
 */
public class LinearLayoutContainer extends LinearLayout implements LoadViewContainer {

    /**
     * 内容加载视图显示位置。
     */
    private int loadViewIndex;

    public LinearLayoutContainer(Context context) {
        this(context, null, 0);
    }

    public LinearLayoutContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadViewIndex = ContentLoadView.getLoadViewIndex(context, attrs, 0);
    }

    @Override
    public int getLoadViewIndex() {
        return loadViewIndex;
    }
}
