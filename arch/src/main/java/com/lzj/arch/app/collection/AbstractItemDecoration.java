/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.collection;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 *
 *
 * @author 吴吉林
 */
public abstract class AbstractItemDecoration extends ItemDecoration {

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        CollectionAdapter adapter = (CollectionAdapter) parent.getAdapter();
        ItemModel model = adapter.getItem(position);
//        Timber.d("position:%s, spanSize:%s", position, model.getSpanSize());
    }
}
