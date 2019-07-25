/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.group;

import android.text.SpannableStringBuilder;

import com.lzj.arch.app.PassiveFragment;

/**
 * 分组页面代表。
 *
 * @author 吴吉林
 */
public interface PageDelegate {

    /**
     * 创建界面。
     *
     * @return 界面
     */
    PassiveFragment createFragment();

    /**
     * 获取名称。
     *
     * @return 名称
     */
    CharSequence getName();

    /**
     * 获取名称。
     *
     * @return 名称
     */
    SpannableStringBuilder getSpannableName();

    /**
     * 获取名称资源 ID。
     *
     * @return 名称资源 ID
     */
    int getNameId();
}
