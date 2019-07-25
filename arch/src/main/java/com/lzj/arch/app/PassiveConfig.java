package com.lzj.arch.app;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;

import com.lzj.arch.R;

import lombok.Getter;
import lombok.Setter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams;
import static com.lzj.arch.util.DisplayUtils.dp2px;

/**
 * 视图配置。
 */
@Setter
@Getter
public class PassiveConfig {

    /**
     * 名称。
     */
    private String name;

    /**
     * 布局资源  ID。
     */
    @LayoutRes
    private int layoutResource;

    /**
     * 标题资源 ID。
     */
    @StringRes
    private int titleResource;

    /**
     * 标题颜色。
     */
    @ColorRes
    private int titleColorRes = R.color.font_black;

    /**
     * 标题栏颜色。
     */
    @ColorRes
    private int toolbarColorRes;

    /**
     * 标题栏颜色,需要在Activity界面配置
     */
    @ColorRes
    private int statusBarColorRes = android.R.color.white;

    /**
     * 菜单资源 ID。
     */
    @MenuRes
    private int menuResource;

    /**
     * 导航图标资源 ID。
     */
    @DrawableRes
    private int navigationIcon = R.drawable.app_toolbar_back_black;

    /**
     * 对话框界面是否可取消。
     */
    private boolean dialogCancelable = true;

    /**
     * 标识是否是底部
     */
    private boolean bottomSheet;

    /**
     * 是否需要自定义的
     */
    private boolean needMySheet;

    /**
     * 对话框界面背景资源 ID。
     */
    @DrawableRes
    private int windowBackground;

    /**
     * 是否全屏
     */
    private boolean fullScreen;

    /**
     * 是否重新绘制 fragment宽高
     */
    private boolean needDimension;

    /**
     * 必须配合 needDimension 才会起作用
     */
    private int[] dialogDimension = new int[]{MATCH_PARENT, WRAP_CONTENT};

    /**
     * 是否包裹对话框内容。
     */
    private boolean wrapDialogContent;

    /**
     * 窗口 DIM 效果是否启用。
     */
    private boolean dimEnabled = true;

    /**
     * 状态栏图标字体颜色是否为深色,需要在Activity界面配置
     */
    private boolean statusBarDarkFont = true;

    /**
     * 状态栏是否透明,需要在Activity界面配置
     */
    private boolean transparentStatusBar;

    /**
     * 软键盘处理，默认处理。需要在Activity界面配置
     */
    private boolean keyboardEnable = true;

    /**
     * 设置该对话框界面的窗口宽高。
     *
     * @param width  宽，dp或 {@link LayoutParams#MATCH_PARENT}或{@link LayoutParams#WRAP_CONTENT}
     * @param height 高，dp或 {@link LayoutParams#MATCH_PARENT}或{@link LayoutParams#WRAP_CONTENT}
     */
    public void setDialogDimension(int width, int height) {
        if (width > 0) {
            width = dp2px(width);
        }
        if (height > 0) {
            height = dp2px(height);
        }
        this.dialogDimension = new int[]{width, height};
    }
}
