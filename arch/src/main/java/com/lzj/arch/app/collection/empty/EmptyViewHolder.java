package com.lzj.arch.app.collection.empty;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzj.arch.R;
import com.lzj.arch.app.collection.AbstractViewHolder;
import com.lzj.arch.app.collection.empty.EmptyItemContract.PassiveView;
import com.lzj.arch.app.collection.empty.EmptyItemContract.Presenter;
import com.lzj.arch.util.ResourceUtils;

/**
 * 空项视图。
 */
public class EmptyViewHolder extends AbstractViewHolder<Presenter> implements PassiveView {

    /**
     * 图片。
     */
    private ImageView image;

    /**
     * 主消息。
     */
    private TextView message;

    /**
     * 次消息。
     */
    private TextView secondMessage;

    /**
     * 重新加载按钮
     */
    private TextView reload;

    /**
     * 自定义按钮
     */
    private TextView actionBtn;

    /**
     * 父控件
     */
    private View parent;

    public EmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        image = findView(R.id.image);
        message = findView(R.id.message);
        secondMessage = findView(R.id.second_message);
        reload = findView(R.id.reload);
        actionBtn = findView(R.id.action_button);
        parent = findView(R.id.parent);
    }

    @Override
    protected void onInitView() {
        super.onInitView();
        if (reload != null) {
            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().onReload();
                }
            });
        }
        if (actionBtn != null) {
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().onActionClicked();
                }
            });
        }
    }

    @Override
    public void showEmpty(int imageResId, String msg, int msgResId, String secondMsg, int secondMsgResId, String action, int actionResId, int backgroundColorId) {
        this.image.setImageResource(imageResId);
        //优先处理有资源id的
        if (msgResId > 0) {
            this.message.setText(msgResId);
        } else {
            if (TextUtils.isEmpty(msg)) {
                this.message.setText(R.string.no_data_message);
            } else {
                this.message.setText(msg);
            }
        }
        if (secondMessage != null) {
            //优先处理有资源id的
            if (secondMsgResId > 0) {
                this.secondMessage.setText(secondMsgResId);
                this.secondMessage.setVisibility(View.VISIBLE);
            } else {
                if (TextUtils.isEmpty(secondMsg)) {
                    this.secondMessage.setVisibility(View.GONE);
                } else {
                    this.secondMessage.setText(secondMsg);
                    this.secondMessage.setVisibility(View.VISIBLE);
                }
            }
        }
        if (actionBtn != null) {
            //优先处理有资源id的
            if (actionResId > 0) {
                this.actionBtn.setText(actionResId);
                this.actionBtn.setVisibility(View.VISIBLE);
            } else {
                if (TextUtils.isEmpty(action)) {
                    this.actionBtn.setVisibility(View.GONE);
                } else {
                    this.actionBtn.setText(action);
                    this.actionBtn.setVisibility(View.VISIBLE);
                }
            }
        }

        if (backgroundColorId != 0 && parent != null) {
            parent.setBackgroundColor(ResourceUtils.getColor(backgroundColorId));
        }
    }
}
