package com.lzj.arch.app.content;

import android.support.annotation.StringRes;

import com.lzj.arch.util.ResourceUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 内容配置类。
 */
@Setter
@Getter
public class ContentConfig {

    /**
     * 空内容标题
     */
    private CharSequence emptyTitle;

    /**
     * 空内容消息
     */
    private CharSequence emptyMessage;

    /**
     * 空内容图片资源 ID。
     */
    private int emptyImage;

    /**
     * 空内容操作文案资源 ID。
     */
    private int emptyAction;

    /**
     * 需要登录跳转入口
     */
    private boolean needLogin;

    public void setEmptyTitle(CharSequence title) {
        this.emptyTitle = title;
    }

    public void setEmptyTitle(@StringRes int resId) {
        this.emptyTitle = ResourceUtils.getString(resId);
    }

    public void setEmptyMessage(CharSequence msg) {
        this.emptyMessage = msg;
    }

    public void setEmptyMessage(@StringRes int resId) {
        this.emptyMessage = ResourceUtils.getString(resId);
    }
}
