package com.lzj.arch.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.lzj.arch.R;

/**
 * 文本截取点击去除下划线.
 *
 * @author gxx
 */

public class NoLineClickSpan extends ClickableSpan {

    public NoLineClickSpan() {
        super();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ResourceUtils.getColor(R.color.primary));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {

    }
}