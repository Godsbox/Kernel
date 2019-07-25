/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.core;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.util.map.StringMapImpl;

import java.util.Map;

import static com.lzj.arch.util.ObjectUtils.requireNonNull;

/**
 * 表现者管理器。
 *
 * @author 吴吉林
 */
public class PresenterManager {

    /**
     * 键：表现者 ID。
     */
    public static final String KEY_PRESENTER_ID = "mvp_presenter_id";

    /**
     * 单例。
     */
    private static final PresenterManager DEFAULT = new PresenterManager();

    /**
     * 错误消息：表现者为null。
     */
    static final String MSG_PRESENTER_IS_NULL = "表现者不能为 null";

    /**
     * 错误消息：不是表现者。
     */
    static final String MSG_NOT_A_PRESENTER = "表现者需要继承 AbstractPresenter 类";

    /**
     * 表现者映射表。
     */
    protected Map<String, Presenter> presenters = new ArrayMap<>();

    /**
     * 获取默认的表现者管理者。
     *
     * @return 表现者管理者
     */
    public static PresenterManager getDefault() {
        return DEFAULT;
    }

    /**
     * 删除表现者。
     *
     * @param presenterId 表现者 ID
     */
    public void remove(String presenterId) {
        presenters.remove(presenterId);
    }

    /**
     * 删除所有的表现者。
     */
    public void removeAll() {
        presenters.clear();
    }

    @SuppressWarnings("unchecked")
    <P extends Presenter> AbstractPresenter getPresenter(String presenterId, Controller<P> controller, Bundle params) {
        AbstractPresenter presenter = (AbstractPresenter) presenters.get(presenterId);
        if (presenter != null) {
            presenter.setFresh(false);
            return presenter;
        }
        P p = controller.createPresenter();
        requireNonNull(p, MSG_PRESENTER_IS_NULL);
        if (!(p instanceof AbstractPresenter)) {
            throw new IllegalStateException(MSG_NOT_A_PRESENTER);
        }
        presenter = (AbstractPresenter) p;
        if (params != null && !params.isEmpty()) {
            presenter.getModel().setParams(new StringMapImpl(new Bundle(params)));
        }
        presenter.setFresh(true);
        presenters.put(presenterId, presenter);
        return presenter;
    }
}
