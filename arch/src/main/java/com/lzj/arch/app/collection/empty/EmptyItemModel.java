package com.lzj.arch.app.collection.empty;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.ItemModel;

import lombok.Getter;
import lombok.Setter;

/**
 * 空项表现模型。
 */
@Getter
@Setter
public class EmptyItemModel extends ItemModel {

    {
        setItemType(R.layout.app_item_empty);
    }

    /**
     * 图片资源 ID。
     */
    @DrawableRes
    private int imageResId = R.mipmap.app_img_no_data;

    /**
     * 主消息资源id。如果String和资源id同时存在，优先使用资源id
     */
    @StringRes
    private int messageResId;

    /**
     * 主消息。如果String和资源id同时存在，优先使用资源id
     */
    private String message;

    /**
     * 次消息资源id。如果String和资源id同时存在，优先使用资源id
     */
    @StringRes
    private int secondMessageResId;

    /**
     * 次消息。如果String和资源id同时存在，优先使用资源id
     */
    private String secondMessage;

    /**
     * 按钮文本
     */
    private String action;

    /**
     * 按钮文本资源id。如果String和资源id同时存在，优先使用资源id
     */
    @StringRes
    private int actionResId;

    /**
     * 用来识别是哪个界面的action点击，配合action按钮使用
     */
    private String actionEvent;

    /**
     * 背景色*/
    private int backgroundColorId;

    /**
     * 用setImageResId(int messageResId)代替
     */
    @Deprecated
    public void setImage(@DrawableRes int imageResId) {
        this.imageResId = imageResId;
    }

    /**
     * 用setMessageResId(int messageResId)代替
     */
    @Deprecated
    public void setMessage(@StringRes int messageResId) {
        this.messageResId = messageResId;
    }

    /**
     * 用setMessage(String message)代替
     */
    @Deprecated
    public void setTip(String message) {
        this.message = message;
    }
}
