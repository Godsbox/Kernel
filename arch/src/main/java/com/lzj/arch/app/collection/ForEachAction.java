/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

/**
 * 遍历操作。
 *
 * @author 吴吉林
 */
public interface ForEachAction<T> {

    /**
     * 处理项模型。
     *
     * @param position 项模型位置
     * @param item 项模型
     * @return true：结束循环；false：继续循环。
     */
    boolean accept(int position, T item);
}
