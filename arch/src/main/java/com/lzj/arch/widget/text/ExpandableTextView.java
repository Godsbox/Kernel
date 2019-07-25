/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import com.lzj.arch.R;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
import static java.lang.Integer.MAX_VALUE;

/**
 * 可以展开更多的文本视图。
 * chagne @time 2018/8/24 15:33
 * @author wsy
 */
public class ExpandableTextView extends AppCompatTextView {

    private int maxLines;
    private BufferType bufferType = BufferType.NORMAL;
    private String text;
    private String moreText;
    private String lessText;
    private int moreColor;
    private int lessColor;

    /**
     * 是否已经展开
     */
    private boolean expand = false;

    /**
     * 原始的最大行数
     */
    private int resMaxLines;

    /**
     * 是否保留换行
     */
    private boolean noWarp = true;

    /**
     * 是否需要处理
     */
    private boolean programmaticChange;

    private OnExpandChangeListener onExpandChangeListener;

    public ExpandableTextView(Context context) {
        this(context, null, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableTextView);
        moreText = a.getString(R.styleable.ExpandableTextView_rmtMoreText);
        lessText = a.getString(R.styleable.ExpandableTextView_rmtLessText);
        moreColor = a.getInteger(R.styleable.ExpandableTextView_rmtMoreColor, Color.BLUE);
        lessColor = a.getInteger(R.styleable.ExpandableTextView_rmtLessColor, Color.BLUE);
        a.recycle();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!programmaticChange) {
            dealText();
        }
        super.onDraw(canvas);
    }

    /**
     * 请务必使用这个方式设置文字
     */
    public void setResText(String content){
        setNeedDealText();
        setText(content);
    }

    public boolean isExpand() {
        return expand;
    }

    private void dealText(){
        Layout layout = getLayout();
        int lineCount = layout.getLineCount();
        if (lineCount > maxLines) {
            lineCount = maxLines;
        }
        if (layout.getEllipsisCount(lineCount - 1) <= 0) {
            setOnClickListener(null);
            return;
        }
        CharSequence summary = createSummary();
        programmaticChange = true;
        setOnClickListener(new OnClick(summary));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.text = text.toString();
        this.bufferType = type;
        super.setText(text, type);
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        expand = maxLines == MAX_VALUE;
        super.setMaxLines(maxLines);
    }

    /**
     * 设置展开状态变化事件监听器。
     *
     * @param listener 展开状态变化事件监听器
     */
    public void setOnExpandChangeListener(OnExpandChangeListener listener) {
        this.onExpandChangeListener = listener;
    }

    private Spanned create(CharSequence content, String label, int color) {
        SpannableStringBuilder contentBuilder = new SpannableStringBuilder(content);
        if (label == null || label.length() == 0) {
            return contentBuilder;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(label);
        builder.setSpan(new ForegroundColorSpan(color), 3, label.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return contentBuilder.append(builder);
    }
    private CharSequence createContent() {
        return create(text, lessText, lessColor);
    }

    private CharSequence createSummary() {
        if (this.moreText == null || this.moreText.length() == 0) {
            callSupperSetText(text);
            return text;
        }
        int len;
        int start;
        Layout layout = getLayout();
        String moreText;
        try{
            moreText = "..." + this.moreText;
            start = layout.getLineStart(maxLines - 1);
            int end = layout.getLineEnd(maxLines - 1) - start;
            CharSequence content;
            if(noWarp){
                content = text.replaceAll("\r\n", " ").subSequence(start, text.replaceAll("\r\n", " ").length());
            } else {
                content = text.subSequence(start, text.length());
            }

            float moreWidth = getPaint().measureText(moreText, 0, moreText.length());
            float maxWidth = layout.getWidth() - moreWidth - 10;
            len = getPaint().breakText(content, 0, content.length(), true, maxWidth, null);
            if (content.length() > end - 1 && content.charAt(end - 1) == '\n') {
                end = end - 1;
            }
            len = Math.min(len, end);
        }catch (Exception e){
            return layout.getText();
        }
        CharSequence summary;
        if(noWarp){
            summary = create(text.replaceAll("\r\n", " ").subSequence(0, start + len), moreText, moreColor);
        } else {
            summary = create(text.subSequence(0, start + len), moreText, moreColor);
        }

        callSupperSetText(summary);
        return summary;
    }

    public void setNeedDealText(){
        programmaticChange = false;
    }

    private void callSupperSetText(CharSequence text) {
        super.setText(text, bufferType);
    }

    public void setResMaxLines(int resMaxLines) {
        this.resMaxLines = resMaxLines;
        setNeedDealText();
        setMaxLines(resMaxLines);
    }

    public void setNoWarp(boolean noWarp) {
        this.noWarp = noWarp;
    }

    private class OnClick implements View.OnClickListener {

        CharSequence summary;
        OnClick(CharSequence s) {
            this.summary = s;
        }

        @Override
        public void onClick(View view) {
            expandText();
        }
    }

    private void expandText(){
        if (!expand) {
            expand = true;
            ExpandableTextView.super.setMaxLines(MAX_VALUE);
            callSupperSetText(createContent());
        } else {
            if(resMaxLines <= 0){
                return;
            }
            expand = false;
            setMaxLines(resMaxLines);
            setNeedDealText();
        }
        if (onExpandChangeListener != null) {
            onExpandChangeListener.onExpandChange(ExpandableTextView.this, expand);
        }
    }

    /**
     * 展开状态变化事件监听器。
     */
    public interface OnExpandChangeListener {

        /**
         * 处理展开变化事件。
         *
         * @param view 视图
         * @param expand true：已展开；false：已收起。
         */
        void onExpandChange(ExpandableTextView view, boolean expand);
    }
}
