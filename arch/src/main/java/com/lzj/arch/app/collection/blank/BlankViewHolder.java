/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.blank;

import android.view.View;

import com.lzj.arch.app.collection.AbstractViewHolder;

/**
 * 空白项视图。
 *
 * @author 吴吉林
 */
public class BlankViewHolder
        extends AbstractViewHolder<BlankItemContract.Presenter>
        implements BlankItemContract.PassiveView {

    public BlankViewHolder(View itemView) {
        super(itemView);
    }
}
        