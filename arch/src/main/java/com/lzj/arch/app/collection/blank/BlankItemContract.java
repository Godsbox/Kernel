/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.blank;

import com.lzj.arch.app.collection.ItemContract;

/**
 * 空白项契约。
 *
 * @author 吴吉林
 */
public interface BlankItemContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ItemContract.PassiveView {

    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ItemContract.Presenter {

    }
}