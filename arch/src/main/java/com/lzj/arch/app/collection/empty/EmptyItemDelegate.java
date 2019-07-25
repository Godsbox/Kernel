package com.lzj.arch.app.collection.empty;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.AbstractItemDelegate;

/**
 * 空项代表。
 */
public class EmptyItemDelegate extends AbstractItemDelegate {

    {
        addLayoutId(R.layout.app_item_empty);
        addLayoutId(R.layout.app_item_empty_second);
        addLayoutId(R.layout.app_item_network_empty);
        addLayoutId(R.layout.app_item_empty_vertical);
        addLayoutId(R.layout.app_item_empty_vertical_little);
        addLayoutId(R.layout.app_item_empty_two_hint_one_button);
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView, int itemType) {
        return new EmptyViewHolder(itemView);
    }
}
