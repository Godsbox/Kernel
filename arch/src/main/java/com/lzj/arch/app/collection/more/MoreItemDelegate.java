/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.collection.more;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.AbstractItemDelegate;

/**
 * 更多项代表。
 *
 * @author 吴吉林
 */
public class MoreItemDelegate extends AbstractItemDelegate {

    {
        addLayoutId(R.layout.app_item_more);
        addLayoutId(R.layout.app_item_more_match);
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView, int itemType) {
        return new MoreViewHolder(itemView);
    }
}
