/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.collection;

import android.support.v4.util.ArrayMap;
import com.lzj.arch.core.AbstractPresenter;
import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.core.PresenterManager;

import java.util.Map;

/**
 * 项管理者。
 *
 * @author 吴吉林
 */
class ItemManager extends PresenterManager {

    /**
     * 项代表映射表。
     */
    private static final ArrayMap<Class<? extends ItemDelegate>, ItemDelegate> DELEGATES = new ArrayMap<>();

    /**
     * 注册项代表。
     *
     * @param delegateClass 项代表类型
     * @return 项管理者
     */
    ItemManager registerItemDelegate(Class<? extends ItemDelegate> delegateClass) {
        if (delegateClass == null || DELEGATES.containsKey(delegateClass)) {
            return this;
        }
        try {
            DELEGATES.put(delegateClass, delegateClass.newInstance());
        } catch (InstantiationException e) {
            //Log.d("wsy","InstantiationException 项代表注册实例化异常");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //Log.d("wsy","InstantiationException 项代表注册安全异常");
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取项类型。
     *
     * @param itemType 项类型
     * @return 项类型
     */
    ItemDelegate getItemDelegate(int itemType) {
        for (int i = 0; i < DELEGATES.size(); i++) {
            ItemDelegate delegate = DELEGATES.valueAt(i);
            if (delegate.matches(itemType)) {
                return delegate;
            }
        }
        return null;
    }

    @Override
    public void removeAll() {
        if (presenters.size() == 0) {
            return;
        }
        for (Map.Entry<String, Presenter> entry : presenters.entrySet()) {
            AbstractPresenter presenter = (AbstractPresenter) entry.getValue();
            presenter.detachView();
            presenter.destroy();
        }
        super.removeAll();
    }
}
