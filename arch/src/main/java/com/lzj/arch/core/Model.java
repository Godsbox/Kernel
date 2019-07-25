/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import com.lzj.arch.util.ObjectUtils;
import com.lzj.arch.util.map.StringMap;

/**
 * 表现模型。
 *
 * @author 吴吉林
 */
public class Model {
    protected final String TAG = getClass().getSimpleName();

    /**
     * 参数表。
     */
    private StringMap params;

    /**
     * 获取参数表。
     *
     * @return 参数表
     */
//    @NonNull
    public StringMap getParams() {
        return ObjectUtils.requireNonNull(params, "该表现模型的参数表是null");
    }


    public boolean haveParams() {
        return params != null;
    }

    /**
     * 设置参数表。
     *
     * @param params 参数表
     */
    void setParams(StringMap params) {
        this.params = params;
    }
}
