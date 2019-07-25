/*
 * Copyright (c) 2017 3000.com All Rights Reserved. 
 */
package com.lzj.arch.app.collection.more;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.lzj.arch.R;
import com.lzj.arch.app.collection.AbstractViewHolder;
import com.lzj.arch.app.collection.more.MoreItemContract.PassiveView;
import com.lzj.arch.app.collection.more.MoreItemContract.Presenter;
import com.lzj.arch.util.ResourceUtils;
import com.lzj.arch.util.ViewUtils;

import static com.lzj.arch.util.ViewUtils.setVisible;

/**
 * 更多项视图。
 *
 * @author 吴吉林
 */
public class MoreViewHolder
        extends AbstractViewHolder<Presenter>
        implements PassiveView {

    /**
     * 提示消息。
     */
    private TextView tip;

    /**
     * 加载中。
     */
    private CircularProgressView progress;

    /**
     * 父布局
     * */
    private View parentLayout;

    /**
     * 没有更多。
     */
    private View noMore;

    private TextView tvNoMore;

    /**
     * 提示消息-图片显示
     * */
    private TextView ivTip;

    /**
     * 提示消息-图片显示_layout
     * */
    private LinearLayout tipLayout;

    public MoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        noMore = findView(R.id.no_more);
        tvNoMore = findView(R.id.no_more_text);
        parentLayout = findView(R.id.more_layout);
        tip = findView(R.id.message);
        ivTip = findView(R.id.message_img);
        tipLayout = findView(R.id.message_img_layout);
        progress = findView(R.id.progress);
    }

    @Override
    public void setProgressVisible(boolean visible) {
        setVisible(progress, visible);
    }

    @Override
    public void setMessage(boolean visible, String message) {
        setVisible(this.tip, visible);
        tip.setText(message);
    }

    @Override
    public void setMoreText(int content){
        if(content > 0)
        tvNoMore.setText(content);
    }

    @Override
    public void setMessage(boolean visible, int message) {
        setVisible(this.tip, visible);
        tip.setText(message);
    }

    @Override
    public void setNoMoreVisible(boolean visible) {
        setVisible(noMore, visible);
    }

    @Override
    public void setMoreBackground(int color) {
        if (parentLayout != null && color > 0) {
            parentLayout.setBackgroundColor(ResourceUtils.getColor(color));
            tvNoMore.setBackgroundColor(ResourceUtils.getColor(color));
        }
    }

    @Override
    public void setNoMoreText(String content) {
        if (tvNoMore != null && content != null) {
            tvNoMore.setText(content);
        }
    }

    @Override
    public void setMoreBackgroundImg(int drawable, String text, int icon, int color) {
        if (drawable > 0) {
            tipLayout.setBackgroundResource(drawable);
        }
        if (text != null) {
            ivTip.setText(text);
        }
        if (icon > 0) {
            ViewUtils.setLeftDrawable(ivTip, icon);
            ivTip.setCompoundDrawablePadding(8);
        }
        if (color > 0) {
            ivTip.setTextColor(ResourceUtils.getColor(color));
        }
    }
}
