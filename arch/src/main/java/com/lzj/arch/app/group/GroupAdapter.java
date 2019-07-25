/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.lzj.arch.app.PassiveFragment;
import com.lzj.arch.util.EmptyUtil;

import static com.lzj.arch.util.ResourceUtils.getString;

/**
 * 分组适配器。
 *
 * @author 吴吉林
 */
public class GroupAdapter extends FragmentStatePagerAdapter {

    /**
     * 分组页面代表集合。
     */
    private SparseArray<PageDelegate> delegates = new SparseArray<>();

    /**
     * 当前分组页面。
     */
    private Fragment currentFragment;

    /**
     * 分组内容界面。
     */
    private Fragment groupFragment;

    public GroupAdapter(FragmentManager fm, Fragment groupFragment) {
        super(fm);
        this.groupFragment = groupFragment;
    }

    @Override
    public Fragment getItem(int position) {
        PageDelegate delegate = delegates.get(position);
        if (delegate == null) {
            return null;
        }
        PassiveFragment fragment = delegate.createFragment();
        fragment.setGroupFragment(groupFragment);
        return fragment;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        if (fragment != currentFragment) {
            currentFragment = fragment;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*
         * 当viewpager多页时，预加载页会不显示。
         * 比如加载第一页，再切换到第七页，再切换到第二页，第二页不加载(显示空白)
         * 取消super可以解决此问题
         * */
//        super.destroyItem(container, position, object);
        PassiveFragment fragment = (PassiveFragment) object;
        fragment.setGroupFragment(null);
    }

    @Override
    public int getCount() {
        return delegates.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PageDelegate delegate = delegates.get(position);
        if (delegate == null) {
            return null;
        }
        int nameId = delegate.getNameId();
        if (!EmptyUtil.isEmpty(delegate.getSpannableName())) {
            return delegate.getSpannableName();
        }
        return EmptyUtil.isEmpty(delegate.getName()) ? getString(nameId) : delegate.getName();
    }

    /**
     * 设置用户可见性。
     *
     * @param isVisibleToUser
     */
    void setUserVisibleHint(boolean isVisibleToUser) {
        if (currentFragment != null) {
            currentFragment.setUserVisibleHint(isVisibleToUser);
        }
    }

    public void addPageDelegate(PageDelegate delegate) {
        delegates.put(delegates.size(), delegate);
    }
}
