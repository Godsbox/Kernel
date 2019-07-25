/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

/**
 * 内容加载监听器。
 *
 * @author 吴吉林
 */
public interface ContentLoadListener {

    /**
     * 处理重新加载单击事件。
     */
    void onReloadClick();

    /**
     * 处理无内容操作单击事件。
     */
    void onEmptyClick();

    /**
     * 添加的自定义动作：暂时只有跳转到登陆
     */
    void onDiyAction();
}
