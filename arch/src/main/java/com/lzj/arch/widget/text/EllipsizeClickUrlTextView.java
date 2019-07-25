/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import com.lzj.arch.util.MyUrlSpan;
import com.lzj.arch.util.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.support.v7.widget.AppCompatTextView;


/**
 * 能够监听是否省略的文本视图。
 *
 * @author wsy
 */
public class EllipsizeClickUrlTextView extends AppCompatTextView {

    /**
     * 省略监听器。
     */
    private OnEllipsizeListener listener;
    private boolean ellipsized;
    private boolean stale;
    private boolean programmaticChange;

    /**
     * 原始文本。
     */
    private String rawText;

    /**
     * url监听
     */
    private OnUrlListener clickableSpan;

    /**
     * 是否已经添加监听
     */
    private boolean needAddUrlListener = true;

    /**
     * 最大行数。
     */
    private int maxLines = -1;

    /**
     * 截取字数
     */
    private int ellipsizeCount;

    /**
     * 临界值
     */
    private int MAX_COUNT = 20;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeClickUrlTextView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeClickUrlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public EllipsizeClickUrlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        this.maxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /**
     * 初始化 setMovementMethod必须设置为null不然获取不到是否省略 每次创建为DynamicLayout
     */
    public void init(){
        stale = true;
        needAddUrlListener = true;
        setMovementMethod(null);
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
        Layout layout = getLayout();
        ellipsizeCount = 0;
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > maxLines) {
                lineCount = maxLines;
            }
            if (lineCount > 0) {
                int line = lineCount - 1;
                int count = layout.getEllipsisCount(line);
                ellipsized = count > 0;
                if (ellipsized) {
                    int ellipsisStart = layout.getEllipsisStart(line);
                    int lineStart = layout.getLineStart(line);
                    ellipsizeCount = lineStart + ellipsisStart;
                    if(ellipsizeCount > resultText.length()){
                        ellipsizeCount = resultText.length();
                    }
                    resultText = resultText.substring(0, ellipsizeCount) + '\u2026';
                }
            }
        }
        if (!resultText.equals(getText())) {
            programmaticChange = true;
            try {
                needAddUrlListener = true;
                if(needAddUrlListener){
                    setContent();
                } else {
                    setText(resultText);
                }
            } finally {
                programmaticChange = false;
            }
        }
        stale = false;

        if (listener != null) {
            listener.onEllipsizeChange(this, this.ellipsized);
        }
        if(needAddUrlListener){
            setContent();
        }
    }

    public void setContent(){
        if(clickableSpan == null){
            return;
        }
        setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = getText();
        if(ellipsized && ellipsizeCount > MAX_COUNT){
            text = text.subSequence(0, ellipsizeCount);
        }
        if (text instanceof Spannable) {
            Spannable sp = (Spannable) getText();
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            // 自己正则匹配
            if(ellipsized && ellipsizeCount > MAX_COUNT){
                style.append("...");
            }
            Pattern p = Pattern.compile(StringUtils.WEB_URL_REGE);
            Matcher m = p.matcher(sp);
            style.clearSpans();
            while(m.find())
            {
                if(m.end() > style.length()){
                    continue;
                }
                String result = m.group();
                MyUrlSpan myURLSpan = new MyUrlSpan(result, clickableSpan);
                style.setSpan(myURLSpan, m.start(),
                        m.end(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            needAddUrlListener = false;
            setText(style);
            programmaticChange = false;
        }
    }

    public void setClickListener(OnUrlListener clickableSpan) {
        this.clickableSpan = clickableSpan;
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
        void onEllipsizeChange(EllipsizeClickUrlTextView view, boolean ellipsized);
    }

}
