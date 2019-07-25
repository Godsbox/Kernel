/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.content;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.lzj.arch.R;
import com.lzj.arch.util.ViewUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.lzj.arch.R.id.load_view_container;
import static com.lzj.arch.util.ViewUtils.findView;
import static com.lzj.arch.util.ViewUtils.setGone;
import static com.lzj.arch.util.ViewUtils.setTopDrawable;
import static com.lzj.arch.util.ViewUtils.setVisible;

/**
 * 内容加载视图。
 *
 * @author 吴吉林
 */
class ContentLoadView extends FrameLayout implements OnClickListener {

    /**
     * 加载开始视图部分。
     */
    private View start;

    /**
     * 加载失败视图部分。
     */
    private View failure;

    /**
     * 加载为空视图部分。
     */
    private View empty;

    /**
     * 加载动态图。
     */
    private CircularProgressView progress;

    /**
     * 重新加载按钮。
     */
    private TextView reload;

    /**
     * 空内容操作。
     */
    private TextView emptyAction;

    /**
     * 失败消息。
     */
    private TextView failureMessage;

    /**
     * 空内容标题。
     */
    private TextView emptyTitle;

    /**
     * 空内容消息。
     */
    private TextView emptyMessage;

    /**
     * 布局文件 ID。
     */
    private int layoutId;

    private TextView goLogin;

    /**
     * 内容加载监听器。
     */
    private ContentLoadListener listener;

    public ContentLoadView(Context context) {
        this(context, R.layout.app_view_content_load);
    }

    public ContentLoadView(Context context, int layoutId) {
        super(context, null);
        this.layoutId = layoutId;
        init();
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        if (v == reload) {
            listener.onReloadClick();
            return;
        }
        if (v == emptyAction) {
            listener.onEmptyClick();
        }
        if (v == goLogin) {
            listener.onDiyAction();
        }
    }

    /**
     * 初始化。
     */
    private void init() {
        if (layoutId == 0) {
            throw new IllegalArgumentException("视图布局文件 ID 不能为0！");
        }

        LayoutInflater.from(getContext()).inflate(layoutId, this, true);
        MarginLayoutParams params = new MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        setLayoutParams(params);
        setBackgroundResource(R.color.window);
        ensureViews();

        goLogin = findViewById(R.id.go_login);
        goLogin.setOnClickListener(this);

        prepareStartPart();
        prepareFailurePart();
        prepareEmptyPart();

        setOnClickListener(this);
    }

    /**
     * 查找并确保三个部分的视图均存在着。
     */
    private void ensureViews() {
        start = findViewById(R.id.load_view_start);
        failure = findViewById(R.id.load_view_failure);
        empty = findViewById(R.id.load_view_empty);

        if (start == null) {
            throw new IllegalArgumentException("找不到开始加载视图部分！");
        }
        if (failure == null) {
            throw new IllegalArgumentException("找不到加载失败视图部分！");
        }
        if (empty == null) {
            throw new IllegalArgumentException("找不到加载为空视图部分！");
        }
    }

    /**
     * 准备开始加载视图部分：查找相应的子视图并作初始化。当子类不采用父类默认的实现时，可以覆写该方法。
     */
    private void prepareStartPart() {
        progress = findView(this, R.id.loading);
    }

    /**
     * 准备加载失败视图部分：查找相应的子视图并作初始化。当子类不采用父类默认的实现时，可以覆写该方法。
     */
    private void prepareFailurePart() {
        reload = findView(this, R.id.reload);
        emptyAction = findView(this, R.id.empty_action);
        failureMessage = findView(this, R.id.content);

        reload.setOnClickListener(this);
        if (emptyAction != null) {
            emptyAction.setOnClickListener(this);
        }
    }

    /**
     * 准备加载为空视图部分：查找相应的子视图并作初始化。当子类不采用父类默认的实现时，可以覆写该方法。
     */
    private void prepareEmptyPart() {
        emptyTitle = findView(empty, R.id.empty_title);
        emptyMessage = findView(empty, R.id.empty_message);
    }

    /**
     * 显示加载开始状态。
     *
     * @param view 父视图
     */
    public void showStart(View view) {
        hideAll();
        start.setVisibility(VISIBLE);
        show(view);
    }

    /**
     * 显示加载失败状态。
     */
    public void showFailure(View view) {
        hideAll();
        failure.setVisibility(VISIBLE);
        show(view);
    }

    /**
     * 显示加载无内容状态。
     */
    public void showEmpty(View view) {
        hideAll();
        empty.setVisibility(VISIBLE);
        show(view);
    }

    private void show(View view) {
        if (getParent() == view || view == null) {
            return;
        }

        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        if (!(view instanceof ViewGroup)) {
            return;
        }
        parent = (ViewGroup) view;

        // 新策略
        if (parent instanceof LoadViewContainer) {
            parent.addView(this, ((LoadViewContainer) parent).getLoadViewIndex());
            return;
        }

        // 旧策略
        int id = parent.getId();
        if (parent instanceof LinearLayout) {
            id = id == load_view_container ? 1 : 0;
            if (parent.getChildCount() == 0) {
                id = 0;
            }
            parent.addView(this, id);
            return;
        }
        parent.addView(this);
    }

    /**
     * 设置空页面提示。
     *
     * @param title   提示语
     * @param message 消息
     * @param image   提示图片资源
     * @param action  操作文案 ID
     */
    void setEmpty(CharSequence title, CharSequence message, @DrawableRes int image, @StringRes int action) {
        if (title != null) {
            emptyTitle.setText(title);
        }
        if (image > 0) {
            setTopDrawable(emptyTitle, image);
        }

        setVisible(emptyMessage, false);
        if (message != null) {
            emptyMessage.setText(message);
            setVisible(emptyMessage, true);
        }

        setGone(emptyAction, false);
        if (emptyAction != null && action > 0) {
            emptyAction.setText(action);
            setGone(emptyAction, true);
        }
    }

    /**
     * 设置是否需要登录跳转
     */
    void setNeedLoginView(boolean need) {
        setVisible(this.goLogin, need);
    }

    /**
     * 消除自己。
     *
     * @param view 父视图
     */
    public void dismiss(View view) {
        if (view == null) {
            return;
        }
        if (!(view instanceof ViewGroup)) {
            return;
        }
        ViewGroup parent = (ViewGroup) view;
        if (getParent() == parent) {
            parent.removeView(this);
        }
    }

    /**
     * 隐藏所有功能视图。
     */
    private void hideAll() {
        start.setVisibility(GONE);
        failure.setVisibility(GONE);
        empty.setVisibility(GONE);
    }

    /**
     * 设置错误内容。
     *
     * @param error 错误内容
     */
    public void setErrorMessage(CharSequence error) {
        if (failureMessage == null || error == null) {
            return;
        }
        failureMessage.setText(error);
        ViewUtils.setGone(reload, !error.toString().contains("评论不存在"));
    }

    /**
     * 设置错误内容。
     *
     * @param id   内容资源 ID
     * @param args 内容格式化参数
     */
    public void setErrorMessage(int id, Object... args) {
        String error = getContext().getString(id, args);
        setErrorMessage(error);
    }

    /**
     * 设置内容加载监听器。
     *
     * @param listener 内容加载监听器
     */
    public void setContentLoadListener(ContentLoadListener listener) {
        this.listener = listener;
    }

    /**
     * 从属性集中获取加载视图显示位置。
     *
     * @param context      上下文
     * @param attrs        属性集
     * @param defaultIndex 默认位置
     * @return 加载视图显示位置
     */
    static int getLoadViewIndex(Context context, AttributeSet attrs, int defaultIndex) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadViewContainer);
        int loadViewIndex = array.getInt(R.styleable.LoadViewContainer_loadViewIndex, defaultIndex);
        array.recycle();
        return loadViewIndex;
    }
}
