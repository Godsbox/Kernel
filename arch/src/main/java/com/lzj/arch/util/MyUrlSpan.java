package com.lzj.arch.util;

import android.view.View;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.lzj.arch.R;
import com.lzj.arch.widget.text.OnUrlListener;


/**
 * 无下划线 点击监听.
 */
public class MyUrlSpan extends ClickableSpan {

    private int color = R.color.primary;

    private String url;

    private OnUrlListener listener;

    public MyUrlSpan(String url, OnUrlListener listener) {
        this.url = url;
        this.listener = listener;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);//无下划线
        ds.setColor(ResourceUtils.getColor(color));
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClickUrl(url);
        }
    }

    public void setColor(int color) {
        this.color = color;
    }
}
