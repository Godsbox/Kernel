/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import com.lzj.arch.util.DisplayUtils;

/**
 * 使TextView中不同大小字体垂直居中
 * @author wsy
 * @time 2018/5/25 11:36
 */
public class CustomVerticalCenterSpan extends ReplacementSpan {

    private int fontSizeSp;    //字体大小sp

    public CustomVerticalCenterSpan(int fontSizeSp){
        this.fontSizeSp = fontSizeSp;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return (int) p.measureText(text.toString());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2) + 2, p);    //此处重新计算y坐标，使字体居中
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(DisplayUtils.sp2px(fontSizeSp));   //设定字体大小, sp转换为px
        return paint;
    }
}