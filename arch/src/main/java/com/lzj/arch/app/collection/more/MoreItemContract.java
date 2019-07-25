/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.collection.more;

import com.lzj.arch.app.collection.ItemContract;

/**
 * 更多项契约。
 *
 * @author 吴吉林
 */
public interface MoreItemContract {

    /**
     * 视图接口。
     */
    interface PassiveView extends ItemContract.PassiveView {

        void setProgressVisible(boolean visible);

        void setMessage(boolean visible, String message);

        void setMoreText(int content);

        void setMessage(boolean visible, int message);

        void setNoMoreVisible(boolean visible);

        void setMoreBackground(int color);

        void setNoMoreText(String content);

        /**
         * 设置背景图*/
        void setMoreBackgroundImg(int drawable, String text, int icon, int color);
    }

    /**
     * 表现者接口。
     */
    interface Presenter extends ItemContract.Presenter {

    }
}
