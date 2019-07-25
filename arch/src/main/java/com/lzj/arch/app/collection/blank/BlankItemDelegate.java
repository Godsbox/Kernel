/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.blank;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.AbstractItemDelegate;

/**
 * 空白项代表。
 *
 * @author 吴吉林
 */
public class BlankItemDelegate extends AbstractItemDelegate {

    {
        addLayoutId(R.layout.app_item_blank);
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView, int itemType) {
        return new BlankViewHolder(itemView);
    }
}
