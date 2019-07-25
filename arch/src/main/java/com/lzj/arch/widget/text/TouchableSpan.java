/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.lzj.arch.R;

/**
 * 可触摸片段。
 *
 * @author 吴吉林
 */
public abstract class TouchableSpan extends ClickableSpan {

    private boolean pressed;

    @ColorInt
    private int pressedBackgroundColor;

    @ColorInt
    private int textColor;

    TouchableSpan(@ColorInt int textColor, @ColorInt int pressedBackgroundColor) {
        this.textColor = textColor;
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    @Override
    public final void onClick(View widget) {
        widget.setTag(R.id.ignore, widget);
        onSpanClick(widget);
    }

    void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(textColor);
        ds.bgColor = pressed ? pressedBackgroundColor : ds.bgColor;
        ds.setUnderlineText(false);
    }

    /**
     * 处理片段单击事件。
     *
     * @param widget 片段所在的视图组件
     */
    protected abstract void onSpanClick(View widget);
}
