/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.database;

/**
 * 数据库调用。
 *
 * @author 吴吉林
 */
public interface Call<R> {

    /**
     * 执行。
     *
     * @param helper 数据库帮助者
     * @return 调用结果
     * @throws Exception
     */
    R execute(DatabaseHelper helper) throws Exception;
}
