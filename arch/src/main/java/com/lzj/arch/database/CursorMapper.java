/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

import android.database.Cursor;

/**
 * 游标映射器。
 *
 * @param <T> 结果类型
 * @author 吴吉林
 */
public interface CursorMapper<T> {

    /**
     * 映射。
     *
     * @param cursor 游标
     * @return 映射结果
     */
    T map(Cursor cursor);
}
