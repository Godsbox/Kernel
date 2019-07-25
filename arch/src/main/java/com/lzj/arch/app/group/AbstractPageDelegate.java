/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.group;

import android.text.SpannableStringBuilder;

/**
 * 页面代表基类。
 *
 * @author 吴吉林
 */
public abstract class AbstractPageDelegate implements PageDelegate {

    /**
     * 分组名称。
     */
    private CharSequence name;

    /**
     * 特殊分组名称。
     */
    private SpannableStringBuilder spannableName;

    /**
     * 分组名称资源 ID。
     */
    private int nameId;

    @Override
    public CharSequence getName() {
        return name;
    }

    /**
     * 设置名称。
     *
     * @param name 名称
     */
    protected void setName(CharSequence name) {
        this.name = name;
    }

    @Override
    public int getNameId() {
        return nameId;
    }

    /**
     * 设置名称资源 ID。
     *
     * @param nameId 名称资源 ID
     */
    protected void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public SpannableStringBuilder getSpannableName() {
        return spannableName;
    }

    public void setSpannableName(SpannableStringBuilder spannableName) {
        this.spannableName = spannableName;
    }
}
