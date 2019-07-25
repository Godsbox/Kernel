/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * 触摸文本移动方法。
 *
 * @author 吴吉林
 */
public final class TouchMovementMethod extends LinkMovementMethod {

    /**
     * 单例。
     */
    private static final TouchMovementMethod INSTANCE = new TouchMovementMethod();

    /**
     * 可触摸文本片段
     */
    private TouchableSpan span;

    private TouchMovementMethod() {
    }

    @Override
    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
            span = getPressedSpan(textView, spannable, event);
            if (span != null) {
                span.setPressed(true);
                Selection.setSelection(spannable, spannable.getSpanStart(span),
                        spannable.getSpanEnd(span));
            }
        } else if (event.getAction() == ACTION_MOVE) {
            TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
            if (span != null && touchedSpan != span) {
                span.setPressed(false);
                span = null;
                Selection.removeSelection(spannable);
            }
        } else {
            if (span != null) {
                span.setPressed(false);
                super.onTouchEvent(textView, spannable, event);
            }
            span = null;
            Selection.removeSelection(spannable);
        }
        return true;
    }

    private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
        TouchableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    /**
     * 获取单例。
     *
     * @return 单例
     */
    public static TouchMovementMethod getInstance() {
        return INSTANCE;
    }
}
