/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;

import com.lzj.arch.util.StringUtils;

/**
 * 能够监听是否省略的文本视图。
 *
 * @author 吴吉林
 */
public class EllipsizeTextView extends AppCompatTextView {

    /**
     * 省略监听器。
     */
    private OnEllipsizeListener listener;
    private boolean ellipsized;
    private boolean stale;
    private boolean programmaticChange;

    /**
     * 是否处理换行
     *  0/ 默认状态 不处理换行  1/ 需要处理换行  2/换行已处理完毕
     */
    private int dealOtherLine = 0;

    /**
     * 原始文本。
     */
    private String rawText;

    /**
     * 最大行数。
     */
    private int maxLines = -1;

    /**
     * 是否在末尾添加换行符
     */
    private boolean addOtherLine;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeTextView(Context context) {
        super(context);
        //maxLines = getMaxLines();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //maxLines = getMaxLines();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //maxLines = getMaxLines();
    }

    /**
     * 设置省略监听器。
     *
     * @param ellipsizeListener 省略监听器
     */
    public void setOnEllipsizeListener(OnEllipsizeListener ellipsizeListener) {
        this.listener = ellipsizeListener;
    }

    @Override
    public void setMaxLines(int maxLines) {
        if (this.maxLines == maxLines) {
            return;
        }
        this.dealOtherLine = 0;
        this.maxLines = maxLines;
        stale = true;
        super.setMaxLines(maxLines);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            rawText = text.toString();
            stale = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (stale) {
            resetText(rawText);
        }
        super.onDraw(canvas);
    }

    private void resetText(String content) {
        String resultText = content;
        boolean ellipsized = false;
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > maxLines) {
                lineCount = maxLines;
            }
            if (lineCount > 0) {
                int line = lineCount - 1;
                ellipsized = layout.getEllipsisCount(line) > 0;
                if (ellipsized) {
                    int ellipsisStart = layout.getEllipsisStart(line);
                    int lineStart = layout.getLineStart(line);
                    int size = lineStart + ellipsisStart;
                    if(size > resultText.length()){
                        size = resultText.length();
                    }
                    resultText = resultText.substring(0, size) + '\u2026';
                }
            }
        }
        if (!resultText.equals(getText())) {
            programmaticChange = true;
            try {
                setText(resultText);
            } finally {
                programmaticChange = false;
            }
        } else if(!this.ellipsized && addOtherLine){
            // 添加换行符  满足8行
            for (int i = 0; i < maxLines -layout.getLineCount(); i++) {
                resultText += "\n";
            }
            setText(resultText);
        }
        stale = false;

        if(dealOtherLine != 2){
            this.ellipsized = ellipsized;
        }

        if(dealOtherLine == 1 && ellipsized){
            dealOtherLine = 2;
            resetText(StringUtils.replaceOtherLine(rawText));
            return;
        }
        if (listener != null) {
            listener.onEllipsizeChange(this, this.ellipsized);
        }
    }

    public void setDealOtherLine(int dealOtherLine) {
        this.dealOtherLine = dealOtherLine;
    }

    public void setAddOtherLine(boolean addOtherLine) {
        this.addOtherLine = addOtherLine;
    }

    /**
     * 省略监听器。
     */
    public interface OnEllipsizeListener {

        /**
         * 监听省略事件。
         *
         * @param view       视图
         * @param ellipsized true：已省略；false：未省略。
         */
        void onEllipsizeChange(EllipsizeTextView view, boolean ellipsized);
    }
}
