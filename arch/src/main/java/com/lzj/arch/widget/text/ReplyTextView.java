/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lzj.arch.R;
import com.lzj.arch.util.MyUrlSpan;
import com.lzj.arch.util.StringUtils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.lzj.arch.R.styleable.ReplyTextView;
import static com.lzj.arch.R.styleable.ReplyTextView_nicknameColor;
import static com.lzj.arch.R.styleable.ReplyTextView_pressedBackgroundColor;
import static com.lzj.arch.R.styleable.ReplyTextView_replyColor;
import static com.lzj.arch.R.styleable.ReplyTextView_replyText;

/**
 * 回复文本视图。
 *
 * @author 吴吉林
 */
public class ReplyTextView extends AppCompatTextView {

    @StringRes
    private int replyText;

    @ColorInt
    private int nicknameColor;

    @ColorInt
    private int replyColor;

    @ColorInt
    private int authorColor;

    /**
     * url监听
     */
    private OnUrlListener clickableSpan;

    /**
     * 最大行数。
     */
    private int maxLines = -1;

    /**
     * 内容最大的字数 （最大行数内）
     */
    private int maxLength = -1;

    @ColorInt
    private int pressedBackgroundColor;

    private OnNicknameClickListener onNicknameClickListener;

    public ReplyTextView(Context context) {
        this(context, null, 0);
    }

    public ReplyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, ReplyTextView, 0, 0);
        replyText = array.getResourceId(ReplyTextView_replyText, 0);
        nicknameColor = array.getColor(ReplyTextView_nicknameColor, 0);
        replyColor = array.getColor(ReplyTextView_replyColor, 0);
        authorColor = context.getResources().getColor(R.color.author_flag);
        pressedBackgroundColor = array.getColor(ReplyTextView_pressedBackgroundColor, 0);
        array.recycle();
    }


    // 处理spannable 和 maxLine 冲突
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        StaticLayout layout = null;
        Field field = null;
        try {
            if (clickableSpan != null) {
                Field staticField = DynamicLayout.class.getDeclaredField("sStaticLayout");
                staticField.setAccessible(true);
                layout = (StaticLayout) staticField.get(DynamicLayout.class);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (layout != null) {
            try {
                if (clickableSpan != null) {
                    field = StaticLayout.class.getDeclaredField("mMaximumVisibleLineCount");
                    field.setAccessible(true);
                    field.setInt(layout, maxLines);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (layout != null && field != null) {
            try {
                if (clickableSpan != null) {
                    int line = maxLines;
                    if (maxLines == -1)
                        maxLines = Integer.MAX_VALUE;
                    field.setInt(layout, line);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置回复。
     *
     * @param nickname   作者昵称
     * @param toNickname 对方昵称
     * @param content    回复内容
     */
    public void setReply(String nickname, String toNickname, String content, boolean author,
                         boolean toAuthor, boolean showTargetNickName) {
        if (TextUtils.isEmpty(nickname)) {
            return;
        }
        boolean toMode = !TextUtils.isEmpty(toNickname) && showTargetNickName;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(nickname);
        int start = 0;
        int preSize = nickname.length(); // 用来截取content 添加省略号
        int end = StringUtils.getLength(nickname);
        builder.setSpan(new NicknameSpan(true), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        if (author) {
            //添加作者标识
            builder.append(getResources().getString(R.string.bracketed_author));
            builder.setSpan(new ForegroundColorSpan(authorColor), end, end + 4, SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new RelativeSizeSpan(0.92f), end, end + 4, SPAN_EXCLUSIVE_EXCLUSIVE);
            end += 4;
        }
        Log.i("--------------------", "toMode:" + toMode + "   toAuthor:" + toAuthor);
        if (toMode) {
            String replyText = getResources().getString(this.replyText);
            builder.append(replyText);
            preSize += replyText.length();
            start = end;
            end = start + replyText.length();
            builder.setSpan(new ForegroundColorSpan(replyColor), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end;
            end = start + toNickname.length();
            builder.append(toNickname);
            preSize += toNickname.length();
            builder.setSpan(new NicknameSpan(false), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

            if (toAuthor) {
                Log.i("--------------------", "toAuthor:" + toAuthor);
                //添加作者标识
                builder.append(getResources().getString(R.string.bracketed_author));
                builder.setSpan(new ForegroundColorSpan(authorColor), end, end + 4, SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new RelativeSizeSpan(0.92f), end, end + 4, SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        String text = getContent(content, preSize);
        builder.append(": ");
        end = builder.length();
        builder.append(text);
        if (clickableSpan != null) {
            Pattern p = Pattern.compile(StringUtils.WEB_URL_REGE);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String result = m.group();
                MyUrlSpan myURLSpan = new MyUrlSpan(result, clickableSpan);
                if (builder.length() < end + m.end()) {
                    continue;
                }
                builder.setSpan(myURLSpan, end + m.start(),
                        end + m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(builder);
    }

    /**
     * 手动添加省略号
     *
     * @param content
     * @param preSize
     * @return
     */
    private String getContent(String content, int preSize) {
        if (maxLength > preSize) {
            int size = maxLength - preSize;
            if (size > content.length()) {
                size = content.length();
            }
            return content.substring(0, size) + '\u2026';
        }
        return content;
    }

    /**
     * 设置昵称单击监听器。
     *
     * @param listener 昵称单击监听器
     */
    public void setOnNicknameClickListener(OnNicknameClickListener listener) {
        this.onNicknameClickListener = listener;
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener listener) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Object ignore = v.getTag(R.id.ignore);
//                if (ignore != null) {
//                    v.setTag(R.id.ignore, null);
//                    return;
//                }
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    public void setClickableSpan(OnUrlListener clickableSpan) {
        this.clickableSpan = clickableSpan;
    }

    /**
     * 昵称片段。
     */
    private class NicknameSpan extends TouchableSpan {

        /**
         * 是否是回复作者。
         */
        private boolean author;

        private NicknameSpan(boolean author) {
            super(nicknameColor, pressedBackgroundColor);
            this.author = author;
        }

        @Override
        protected void onSpanClick(View widget) {
            if (onNicknameClickListener != null) {
                onNicknameClickListener.onNickNameClick(ReplyTextView.this, author);
            }
        }
    }

    /**
     * 昵称单击监听器。
     */
    public interface OnNicknameClickListener {

        /**
         * 处理昵称单击事件。
         *
         * @param view   回复视图
         * @param author 是否是回复作者，true：是作者；false：对方。
         */
        void onNickNameClick(ReplyTextView view, boolean author);
    }
}
