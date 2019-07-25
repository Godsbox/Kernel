/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.blank;

import com.lzj.arch.app.collection.ItemPresenter;
import com.lzj.arch.core.Contract;

/**
 * 空白项表现者。
 *
 * @author 吴吉林
 */
public class BlankItemPresenter
        extends ItemPresenter<BlankItemContract.PassiveView, BlankItemModel, Contract.Router>
        implements BlankItemContract.Presenter {
}