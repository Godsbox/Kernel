/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.lzj.arch.R;

/**
 * 未读绘制。
 *
 * @author 吴吉林
 */
public class UnreadDrawable extends Drawable {

    private Drawable drawable;
    private boolean unread;
    private Paint paint;
    private int radius;

    public UnreadDrawable(Context context, Drawable origin) {
        drawable = origin;// 原来的drawable
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.red));
        //小红点半径
        radius = context.getResources().getDimensionPixelSize(R.dimen.radius_round);
    }

    public void setUnread(boolean unread) {
        if (this.unread == unread) {
            return;
        }
        this.unread = unread;
        invalidateSelf();
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawable.draw(canvas);//先绘制原图标
        if (!unread) {
            return;
        }
        // 获取原图标的右上角坐标
        int cx = getBounds().right;
        int cy = getBounds().top;
        // 计算我们的小红点的坐标
        cy += radius;
        cx -= radius;
        canvas.drawCircle(cx, cy, radius, paint);//绘制小红点
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

    @Override
    public int getIntrinsicHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
        drawable.setBounds(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        drawable.setBounds(left, top, right, bottom);
    }

    public static UnreadDrawable wrap(Context context, Drawable drawable) {
        if (drawable instanceof UnreadDrawable) {
            return (UnreadDrawable) drawable;
        }
        return new UnreadDrawable(context, drawable);
    }
}
