/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection.more;

import android.content.Intent;
import android.support.annotation.ColorRes;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.ItemModel;
import com.lzj.arch.util.ResourceUtils;

/**
 * 更多项表现模型。
 *
 * @author 吴吉林
 */
public class MoreItemModel extends ItemModel {

    /**
     * 标识是否还有更多。
     */
    private boolean hasMore;

    /**
     * 提示消息。
     */
    private String message;

    /**
     * 提示消息资源 ID。
     */
    private int messageId;

    /**
     * 标识是否正在加载更多。
     */
    private boolean moreLoading;

    /**
     * “没有更多”的点击操作
     */
    private Intent moreActionIntent;

    /**
     * “没有更多”的背景颜色
     */
    private int moreBackground;

    /**
     * “没有更多”的背景图
     */
    private int moreBackgroundImg;

    /**
     * “没有更多”文案
     */
    private String noMoreText = ResourceUtils.getString(R.string.load_more_empty);

    /**
     * “没有更多”的内容
     */
    private int content;

    /**
     * 背景的文字
     */
    private String moreBackgroundImgText;

    /**
     * 背景的小图标
     */
    private int moreBackgroundImgIcon;

    /**
     * 背景的文字颜色
     */
    private int moreBackgroundImgTextColor;

    {
        setItemType(R.layout.app_item_more);
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMoreLoading() {
        return moreLoading;
    }

    public void setMoreLoading(boolean moreLoading) {
        this.moreLoading = moreLoading;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Intent getMoreActionIntent() {
        return moreActionIntent;
    }

    public void setMoreActionIntent(Intent moreActionIntent) {
        this.moreActionIntent = moreActionIntent;
    }

    public int getMoreBackground() {
        return moreBackground;
    }

    public void setMoreBackground(int moreBackground) {
        this.moreBackground = moreBackground;
    }

    public int getMoreBackgroundImg() {
        return moreBackgroundImg;
    }

    public void setMoreBackgroundImg(int moreBackgroundImg) {
        this.moreBackgroundImg = moreBackgroundImg;
    }

    public String getMoreBackgroundImgText() {
        return moreBackgroundImgText;
    }

    public void setMoreBackgroundImgText(String moreBackgroundImgText) {
        this.moreBackgroundImgText = moreBackgroundImgText;
    }

    public int getMoreBackgroundImgIcon() {
        return moreBackgroundImgIcon;
    }

    public void setMoreBackgroundImgIcon(int moreBackgroundImgIcon) {
        this.moreBackgroundImgIcon = moreBackgroundImgIcon;
    }

    public String getNoMoreText() {
        return noMoreText;
    }

    public void setNoMoreText(String noMoreText) {
        this.noMoreText = noMoreText;
    }

    public void setMoreContent(int content) {
        this.content = content;
    }

    public int getMoreContent() {
        return content;
    }

    public int getMoreBackgroundImgTextColor() {
        return moreBackgroundImgTextColor;
    }

    public void setMoreBackgroundImgTextColor(@ColorRes int moreBackgroundImgTextColor) {
        this.moreBackgroundImgTextColor = moreBackgroundImgTextColor;
    }
}
