package com.lzj.arch.app.collection.empty;

import android.support.annotation.DrawableRes;

import com.lzj.arch.app.collection.ItemContract;

/**
 * 空项契约。
 */
public interface EmptyItemContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ItemContract.PassiveView {

        /**
         * 显示空项。
         */
        void showEmpty(@DrawableRes int imageResId,
                       String msg, int msgResId,
                       String secondMsg, int secondMsgResId,
                       String action, int actionResId,
                       int backgroundColorId);
    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ItemContract.Presenter {

        /**
         * 重新加载
         */
        void onReload();

        /**
         * 按钮被点击
         */
        void onActionClicked();
    }
}
