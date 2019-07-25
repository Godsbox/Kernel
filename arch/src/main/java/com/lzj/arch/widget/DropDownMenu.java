package com.lzj.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzj.arch.R;
import com.lzj.arch.util.DisplayUtils;

import java.util.List;

import static com.lzj.arch.util.DisplayUtils.dp2px;

/**
 * 下拉式菜单组件
 */
public class DropDownMenu extends LinearLayout {

    /**
     * 顶部菜单布局
     */
    private LinearLayout tabMenuView;

    /**
     * 底部容器，包含popupMenuViews，maskView
     */
    private FrameLayout containerView;

    /**
     * 弹出菜单父布局
     */
    private FrameLayout popupMenuViews;

    /**
     * 遮罩半透明View，点击可关闭DropDownMenu
     */
    private View maskView;

    /**
     * tabMenuView里面选中的tab位置，-1表示未选中
     */
    private int current_tab_position = -1;

    /**
     * 分割线颜色
     */
    private int dividerColor = 0xffcccccc;

    /**
     * tab选中颜色
     */
    private int textSelectedColor = 0xff890c85;

    /**
     * tab未选中颜色
     */
    private int textUnselectedColor = 0xff111111;

    /**
     * 遮罩颜色
     */
    private int maskColor = 0x88888888;

    /**
     * tab字体大小
     */
    private static int menuTextSize = 14;

    /**
     * tab选中图标
     */
    private int menuSelectedIcon;

    /**
     * tab未选中图标
     */
    private int menuUnselectedIcon;

    /**
     * tab高度
     */
    private int tabHeight;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        //为DropDownMenu添加自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        int underlineColor = a.getColor(R.styleable.DropDownMenu_underlineColor, ContextCompat.getColor(context, R.color.font));
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        int menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, ContextCompat.getColor(context, R.color.white));
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        tabHeight = a.getDimensionPixelSize(R.styleable.DropDownMenu_tabHeight, DisplayUtils.dp2px(50));
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts   选项文字集合
     * @param popupViews 弹出的视图集合
     */
    public void setDropDownMenu(@NonNull List<? extends DropDown> tabTexts, @NonNull List<View> popupViews) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 0);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 1);

        for (int i = 0; i < popupViews.size(); i++) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.bottomMargin = dp2px(150);
            popupViews.get(i).setLayoutParams(params);
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    /**
     * 添加一个tab
     *
     * @param tabTexts tab列表
     * @param position tab位置
     */
    private void addTab(@NonNull List<? extends DropDown> tabTexts, int position) {
        final FrameLayout tab = new FrameLayout(getContext());
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        final TextView text = new TextView(getContext());
        text.setSingleLine();
        text.setEllipsize(TextUtils.TruncateAt.END);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        text.setTextColor(textUnselectedColor);
        text.setCompoundDrawablePadding(dp2px(4));
        text.setCompoundDrawablesWithIntrinsicBounds(0, 0, menuUnselectedIcon, 0);
        text.setText(tabTexts.get(position).getName());
        text.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, tabHeight, Gravity.CENTER));
        text.setGravity(Gravity.CENTER);
        //将文本视图添加到Tab布局中
        tab.addView(text);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
        //添加分割线
        if (position < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dp2px(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文本
     *
     * @param text 文本
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            getTextViewFromTab(current_tab_position).setText(text);
        }
    }

    /**
     * 设置第一个Tab的文本
     *
     * @param text
     */
    public void setFirstTabText(String text) {
        current_tab_position = 0;
        setTabText(text);
    }

    /**
     * 设置是否能点击
     *
     * @param clickable true:可点击 false:不可点击
     */
    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            getTextViewFromTab(current_tab_position).setTextColor(textUnselectedColor);
            getTextViewFromTab(current_tab_position).setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    menuUnselectedIcon, 0);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.app_interpolator_menu_out));
            maskView.setVisibility(GONE);
            //遮罩退出动画
            AlphaAnimation maskOut = new AlphaAnimation(1, 0);
            maskOut.setDuration(250);
            maskView.setAnimation(maskOut);
            current_tab_position = -1;
        }
    }

    /**
     * 切换菜单
     *
     * @param target 目标菜单
     */
    private void switchMenu(View target) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.app_interpolator_menu_in));
                        maskView.setVisibility(VISIBLE);
                        //遮罩进入动画
                        AlphaAnimation maskIn = new AlphaAnimation(0, 1);
                        maskIn.setDuration(250);
                        maskView.setAnimation(maskIn);
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    getTextViewFromTab(i).setTextColor(textSelectedColor);
                    getTextViewFromTab(i).setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            menuSelectedIcon, 0);
                }
            } else {
                getTextViewFromTab(i).setTextColor(textUnselectedColor);
                getTextViewFromTab(i).setCompoundDrawablesWithIntrinsicBounds(0, 0, menuUnselectedIcon, 0);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 从Tab布局中取到文本视图
     *
     * @param position 位置
     * @return 文本视图
     */
    private TextView getTextViewFromTab(int position) {
        return (TextView) ((ViewGroup) tabMenuView.getChildAt(position)).getChildAt(0);
    }

}
