/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.group;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.lzj.arch.R;
import com.lzj.arch.app.content.ContentFragment;
import com.lzj.arch.app.group.GroupContract.PassiveView;
import com.lzj.arch.app.group.GroupContract.Presenter;
import com.lzj.arch.util.ResourceUtils;
import com.lzj.arch.util.ViewUtils;


/**
 * 分组内容界面。
 *
 * @author 吴吉林
 */
public abstract class GroupFragment<P extends Presenter>
        extends ContentFragment<P>
        implements PassiveView {

    /**
     * 翻页视图。
     */
    private ViewPager pager;

    /**
     * 标签布局。
     */
    private TabLayout tabLayout;

    /**
     * tabLayout 的边距 下划线的宽度
     */
    private int tabLayoutPadding;

    /**
     * 分组适配器。
     */
    private GroupAdapter adapter;

    /**
     * 是否需要tab选中事件
     */
    private boolean needTabSelectedListener;

    /**
     * 使用新的tab选中样式
     */
    private boolean needNewTabSelectedStyle;

    /**
     * 翻页视图 ID。
     */
    private int pagerId = R.id.pager;

    /**
     * 默认缓存页面数
     */
    private int DEFAULT_CACHE_PAGE_SIZE = 3;

    /**
     * 使用 child manager
     */
    private boolean useChildManager;

    {
        getConfig().setLayoutResource(R.layout.app_fragment_group);
    }

    @Override
    public void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        if (useChildManager) {
            adapter = new GroupAdapter(getChildFragmentManager(), this);
        } else {
            adapter = new GroupAdapter(getFragmentManager(), this);
        }
    }

    @CallSuper
    @Override
    public void onFindView() {
        super.onFindView();
        pager = findView(R.id.pager);
        tabLayout = findView(R.id.tab_layout);
    }

    @CallSuper
    @Override
    public void onInitView(Bundle state) {
        super.onInitView(state);
        pager.setId(pagerId);
        pager.addOnPageChangeListener(new OnPageChangeListener());
        pager.setOffscreenPageLimit(DEFAULT_CACHE_PAGE_SIZE);
        onInitAdapter();
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        if (needTabSelectedListener) {
            tabLayout.addOnTabSelectedListener(new OnTabLayoutSelectedListener());
        }
        if (tabLayoutPadding != 0) {
            setTabLayoutPadding();
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        pager.addOnPageChangeListener(listener);
    }

    public void setCachePageSize(int size) {
        this.DEFAULT_CACHE_PAGE_SIZE = size;
    }

    public void setNeedTabSelectedListener(boolean needTabSelectedListener) {
        this.needTabSelectedListener = needTabSelectedListener;
    }

    public Fragment getFragment(int position) {
        return (Fragment) adapter.instantiateItem(pager, position);
    }

    /**
     * 设置TabLayout下划线宽度
     */
    public void setTabLayoutPadding() {
        if (tabLayout != null) {
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    ViewUtils.setIndicator(tabLayout, tabLayoutPadding, tabLayoutPadding);
                }
            });
        }
    }

    @Override
    public void notifyPagerChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showFragment(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (adapter != null) {
            adapter.setUserVisibleHint(isVisibleToUser);
        }
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    /**
     * 更新tabLayout tab
     *
     * @param tabView
     * @param position
     */
    public void updateTab(View tabView, int position) {
        if (tabView == null) {
            return;
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);

        // 更新Badge前,先remove原来的customView,否则Badge无法更新
        View customView = tab.getCustomView();
        if (customView != null) {
            ViewParent parent = customView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(customView);
            }
        }
        // 更新CustomView
        tab.setCustomView(tabView);

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        if (tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView() == null) {
            return;
        }
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }

    public CharSequence getTabTitle(int position) {
        return adapter.getPageTitle(position);
    }

    /**
     * 初始化适配器。
     */
    protected abstract void onInitAdapter();

    /**
     * 添加分组页面代表。
     *
     * @param delegate 分组页面代表
     */
    protected void addPageDelegate(PageDelegate delegate) {
        if (adapter != null) {
            adapter.addPageDelegate(delegate);
        }
    }

    @Override
    public int getCurrentItem() {
        if (pager != null)
            return pager.getCurrentItem();
        return 0;
    }

    /**
     * 设置翻页视图 ID。
     *
     * @param pagerId 翻页视图 ID
     */
    protected void setPagerId(int pagerId) {
        this.pagerId = pagerId;
    }

    public void setTabLayoutPadding(int tabLayoutPadding) {
        this.tabLayoutPadding = tabLayoutPadding;
    }

    public void setUseChildManager(boolean useChildManager) {
        this.useChildManager = useChildManager;
    }

    public void setNeedNewTabSelectedStyle(boolean needNewTabSelectedStyle) {
        this.needNewTabSelectedStyle = needNewTabSelectedStyle;
    }

    /**
     * 页面变化监听器。
     */
    private class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            getPresenter().onPageChange(position);
        }
    }

    /**
     * 更新TabLayout tabView为自定义的view  R.layout.app_tab_text_item
     */
    public void updateTabViewCustom(){
        if(tabLayout == null){
            return;
        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.app_tab_text_item, null);
            // ,先remove原来的customView,否则无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            TextView textView = tabView.findViewById(R.id.tab_text);
            textView.setText(getTabTitle(i));
            if(tabLayout.getSelectedTabPosition() == i){
                textView.setTextSize(18);
                textView.setTextColor(ResourceUtils.getColor(R.color.tab_text_selected_color));
            } else {
                textView.setTextSize(14);
                textView.setTextColor(ResourceUtils.getColor(R.color.tab_text_color));
            }
            // 更新CustomView
            tab.setCustomView(textView);

            // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
            if (tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView() == null) {
                return;
            }
            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
        }
    }

    /**
     * 更新TabLayout tabView为自定义的view  R.layout.app_tab_text_item
     */
    public void updateTabViewCustom(int position, @ColorRes int selColor, int selSize, @ColorRes int unSelColor, int unSelSize) {
        if(tabLayout == null){
            return;
        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.app_tab_text_item, null);
            // ,先remove原来的customView,否则无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            TextView textView = tabView.findViewById(R.id.tab_text);
            textView.setText(getTabTitle(i));
            if(position == i){
                textView.setTextSize(selSize);
                textView.setTextColor(ResourceUtils.getColor(selColor));
            } else {
                textView.setTextSize(unSelSize);
                textView.setTextColor(ResourceUtils.getColor(unSelColor));
            }
            ViewUtils.setBoldText(textView, position == i);
            // 更新CustomView
            tab.setCustomView(textView);

            // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
            if (tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView() == null) {
                return;
            }
            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
        }
    }

    /**
     * tab选中监听器。
     */
    private class OnTabLayoutSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if(needNewTabSelectedStyle){
                View view = tab.getCustomView();
                if(view != null){
                    TextView textView = view.findViewById(R.id.tab_text);
                    textView.setTextSize(18);
                    textView.setTextColor(ResourceUtils.getColor(R.color.tab_text_selected_color));
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if(needNewTabSelectedStyle){
                View view = tab.getCustomView();
                if(view != null){
                    TextView textView = view.findViewById(R.id.tab_text);
                    textView.setTextSize(14);
                    textView.setTextColor(ResourceUtils.getColor(R.color.tab_text_color));
                }
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            getPresenter().onTabReselected(tab.getPosition());
        }
    }
}
