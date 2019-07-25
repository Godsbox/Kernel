package com.lzj.arch.app;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.lzj.arch.core.Contract;
import com.lzj.arch.util.map.StringMap;

/**
 * 被动契约。
 */
public interface PassiveContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends Contract.PassiveView {

        /**
         * 设置标题。
         *
         * @param title 标题
         */
        void setTitle(String title);

        /**
         * 设置工具栏标题。
         *
         * @param title 标题资源 ID
         */
        void setTitle(int title);

        /**
         * 设置toolbar背景颜色
         *
         * @param resId 颜色id
         */
        void setToolbarResource(@DrawableRes int resId);

        /**
         * 设置toolbar背景颜色
         *
         * @param colorId 颜色id
         */
        void setToolbarColor(@ColorInt int colorId);

        /**
         * 获得应用栏颜色
         */
        @Deprecated
        int getAppbarColor();

        /**
         * 设置应用栏显示/隐藏。
         */
        void setAppbarVisible(int visible);
    }

    /**
     * 表现者接口。
     */
    interface Presenter extends Contract.Presenter {

        /**
         * 处理成功结果。
         *
         * @param requestCode 请求代码
         * @param data        结果数据
         */
        void onOkResult(int requestCode, StringMap data);

        /**
         * 处理取消结果。
         *
         * @param requestCode 请求代码
         */
        void onCanceledResult(int requestCode);
    }
}
