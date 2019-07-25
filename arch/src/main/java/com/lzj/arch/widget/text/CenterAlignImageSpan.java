/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * 使TextView中插入图片垂直居中
 * @author wsy
 * @time 2018/8/17 10:03
 */
public class CenterAlignImageSpan extends ImageSpan {

    public CenterAlignImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenterAlignImageSpan(Bitmap b) {
        super(b);
    }

    public CenterAlignImageSpan(Context context, int drawable){
        super(context, drawable);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
        Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;//计算y方向的位移
        canvas.save();
        canvas.translate(x, transY);//绘制图片位移一段距离
        b.draw(canvas);
        canvas.restore();
    }
}