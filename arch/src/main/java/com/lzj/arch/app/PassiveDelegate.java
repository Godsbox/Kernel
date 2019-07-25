package com.lzj.arch.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.lzj.arch.R;
import com.lzj.arch.app.PassiveContract.Presenter;
import com.lzj.arch.core.Contract;
import com.lzj.arch.util.ViewUtils;
import com.lzj.arch.util.map.StringMap;
import com.lzj.arch.util.map.StringMapImpl;

/**
 * 视图委托。
 */
public class PassiveDelegate<P extends Contract.Presenter> implements View.OnClickListener {
    /**
     * 日志用的TAG
     */
    protected final String TAG = getClass().getSimpleName();
    /**
     * 视图配置。
     */
    private PassiveConfig config = new PassiveConfig();

    /**
     * 应用栏。
     */
    private Toolbar toolbar;

    /**
     * 控制器。
     */
    private PassiveController<P> controller;

    public PassiveDelegate(PassiveController<P> controller) {
        this.controller = controller;
    }

    PassiveConfig getConfig() {
        return config;
    }

    void findView(Activity activity) {
        toolbar = activity.findViewById(R.id.appbar);
    }

    void findView(Fragment fragment) {
        toolbar = ViewUtils.findView(fragment.getView(), R.id.appbar);
    }

    void initView(Activity activity) {
        if (toolbar == null) {
            return;
        }
        if (config.getTitleResource() > 0) {
            toolbar.setTitle(config.getTitleResource());
        }
        if (config.getTitleColorRes() > 0) {
            toolbar.setTitleTextColor(activity.getResources().getColor(config.getTitleColorRes()));
        }
        if (config.getNavigationIcon() > 0) {
            toolbar.setNavigationIcon(config.getNavigationIcon());
            toolbar.setNavigationOnClickListener(this);
        }
        if (config.getMenuResource() > 0) {
            toolbar.inflateMenu(config.getMenuResource());
            toolbar.setOnMenuItemClickListener(controller);
        }
        if (config.getToolbarColorRes() > 0) {
            setToolbarResource(config.getToolbarColorRes());
        }
    }

    public void setToolbarTitle(@StringRes int title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setToolbarVisible(int visible) {
        if (toolbar != null) {
            toolbar.setVisibility(visible);
        }
    }

    public void setToolbarResource(@DrawableRes int resId) {
        if (toolbar != null) {
            toolbar.setBackgroundResource(resId);
        }
    }

    public void setToolbarColor(@ColorInt int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }

    public int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Activity activity = controller instanceof Activity
                    ? (Activity) controller
                    : ((Fragment) controller).getActivity();
            return activity.getWindow().getStatusBarColor();
        }
        return 0;
    }

    public void setFullScreen(boolean full) {
        Activity activity = controller instanceof Activity
                ? (Activity) controller
                : ((Fragment) controller).getActivity();
        if (full) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public Menu getMenu() {
        if (toolbar == null) {
            throw new IllegalStateException("当前界面没有应用栏");
        }
        return toolbar.getMenu();
    }

    @Override
    public void onClick(View v) {
        controller.onNavigationClick();
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        Contract.Presenter p = controller.getPresenter();
        if (!(p instanceof PassiveContract.Presenter)) {
            return;
        }
        Presenter presenter = (Presenter) controller.getPresenter();
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putString("data", data.getDataString());
            StringMap map = new StringMapImpl(extras);
            presenter.onOkResult(requestCode, map);
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            presenter.onCanceledResult(requestCode);
        }
    }
}
