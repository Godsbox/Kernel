/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import static com.lzj.arch.R.styleable.FixedPixelImageView;
import static com.lzj.arch.R.styleable.FixedPixelImageView_fixedHeight;
import static com.lzj.arch.R.styleable.FixedPixelImageView_fixedWidth;
import static com.lzj.arch.R.styleable.FixedPixelImageView_widthCharge;
import static com.lzj.arch.util.ViewUtils.getFitHeightDimension;
import static com.lzj.arch.util.ViewUtils.getFitWidthDimension;

/**
 * 固定像素图片视图。
 *
 * @author 吴吉林
 */
public class FixedPixelImageView extends AppCompatImageView {

    /**
     * 固定宽度。（单位：像素）
     */
    private int mFixedWidth;

    /**
     * 固定高度。（单位：像素）
     */
    private int mFixedHeight;

    /**
     * 是否由宽度决定。
     */
    private boolean mWidthCharge;

    public FixedPixelImageView(Context context) {
        this(context, null);
    }

    public FixedPixelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedPixelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性集。
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, FixedPixelImageView, 0, 0);
        mFixedWidth = array.getInteger(FixedPixelImageView_fixedWidth, 720);
        mFixedHeight = array.getInteger(FixedPixelImageView_fixedHeight, 360);
        mWidthCharge = array.getBoolean(FixedPixelImageView_widthCharge, true);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] dimension = mWidthCharge
                ? getFitWidthDimension(widthMeasureSpec, mFixedWidth, mFixedHeight)
                : getFitHeightDimension(heightMeasureSpec, mFixedWidth, mFixedHeight);
        setMeasuredDimension(dimension[0], dimension[1]);
    }

    public void setFixedHeight(int fixedHeight) {
        mFixedHeight = fixedHeight;
    }

    public void setFixedWidth(int fixedWidth) {
        mFixedWidth = fixedWidth;
    }
}
