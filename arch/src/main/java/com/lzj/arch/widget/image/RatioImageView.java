/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.lzj.arch.R;

/**
 * 比例图片视图。
 *
 * @author 吴吉林
 */
public class RatioImageView extends AppCompatImageView {

    /**
     * 宽高比例。
     */
    private float ratio;

    public RatioImageView(Context context) {
        this(context, null, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, 0, 0);
        ratio = array.getFloat(R.styleable.RatioImageView_imageRatio, 0.0f);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / ratio);
        setMeasuredDimension(width, height);
    }
}
