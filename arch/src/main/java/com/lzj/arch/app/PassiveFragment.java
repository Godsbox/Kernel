package com.lzj.arch.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lzj.arch.app.lifecycle.FragmentLifecycle;
import com.lzj.arch.app.lifecycle.LifecycleManager;
import com.lzj.arch.core.Contract.Presenter;
import com.lzj.arch.core.Contract.Router;
import com.lzj.arch.core.PresenterDelegate;
import com.lzj.arch.core.PresenterManager;
import com.lzj.arch.util.ResourceUtils;
import com.lzj.arch.view.MyBottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
import static com.lzj.arch.core.Arch.newPresenterDelegate;

/**
 * 被动界面。<br /><br />
 *
 * @param <P> 表现者类型，如果不想使用表现者功能，该参数可以传 {@link Presenter}。
 */
public abstract class PassiveFragment<P extends Presenter>
        extends DialogFragment
        implements PassiveController<P> {
    /**
     * 日志用的TAG
     */
    protected final String TAG = getClass().getSimpleName();
    /**
     * 视图委托。
     */
    private PassiveDelegate<P> passiveDelegate = new PassiveDelegate<>(this);

    /**
     * 表现者委托。
     */
    private PresenterDelegate<P> presenterDelegate = newPresenterDelegate(this);

    /**
     * 界面生命周期。
     */
    private FragmentLifecycle lifecycle;

    /**
     * 分组内容界面。
     */
    private Fragment groupFragment;

    /**
     * 旧的用户可见性。
     */
    private boolean setUserVisibleHintWasCalled;

    private final String USER_VISIBLE = "user_visible";

    /**
     * 新的用户可见性
     */
    private boolean userVisible;

    /**
     * ButterKnife解绑器
     */
    private Unbinder unbinder;

    {
        setArguments(new Bundle());
    }

    /**
     * 获取界面生命周期。
     *
     * @return 界面生命周期
     */

    private FragmentLifecycle getDiyLifecycle() {
        if (lifecycle == null) {
            lifecycle = LifecycleManager.getInstance().createFragmentLifecycle();
        }
        return lifecycle;
    }

    /**
     * 设置界面生命周期。
     *
     * @param lifecycle 界面生命周期
     */
    protected void setLifecycle(FragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        getDiyLifecycle().onCreate(this, state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setUserVisibleHintWasCalled = savedInstanceState.getBoolean(USER_VISIBLE);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout = getConfig().getLayoutResource();
        if (layout <= 0) {
            return null;
        }
        View view = inflater.inflate(layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        passiveDelegate.findView(this);
        passiveDelegate.initView(getActivity());
        onFindView();
        onInitView(state);
        presenterDelegate.onViewCreate(this, state, getArguments(), PresenterManager.getDefault());
        presenterDelegate.attachView(this, getUserVisibleHint());
    }

    @Override
    public void onFindView() {
        // 空实现
    }

    @Override
    public void onInitView(Bundle state) {
        // 空实现
    }

    @Override
    public final <V> V findView(int id) {
        View view = getView();
        if (view == null) {
            return null;
        }
        return (V) view.findViewById(id);
    }

    /**
     * 设置当前屏幕的返回结果。
     *
     * @param resultCode 结果代码
     * @param data       结果数据
     */
    protected void setResult(int resultCode, Intent data) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.setResult(resultCode, data);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getConfig().isBottomSheet()) {
            return getConfig().isNeedMySheet() ? new MyBottomSheetDialog(getContext()) :
                    new BottomSheetDialog(getContext());
        } else {
            return new AppCompatDialog(getContext(), 0);
        }
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater inflater = super.onGetLayoutInflater(savedInstanceState);
        if (getShowsDialog()) {
            Dialog dialog = getDialog();
            dialog.setCancelable(getConfig().isDialogCancelable());
            setCancelable(getConfig().isDialogCancelable());
            dialog.requestWindowFeature(FEATURE_NO_TITLE);
            if (!getConfig().isDimEnabled()) {
                dialog.getWindow().clearFlags(FLAG_DIM_BEHIND);
            }
        }
        return inflater;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        if (dialog instanceof AppCompatDialog) {
            // If the dialog is an AppCompatDialog, we'll handle it
            AppCompatDialog acd = (AppCompatDialog) dialog;
            switch (style) {
                case STYLE_NO_INPUT:
                    dialog.getWindow().addFlags(FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCHABLE);
                    // fall through...
                case STYLE_NO_FRAME:
                case STYLE_NO_TITLE:
                    acd.supportRequestWindowFeature(FEATURE_NO_TITLE);
            }
            return;
        }

        // Else, just let super handle it
        super.setupDialog(dialog, style);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getShowsDialog() && getDialog() != null) {
            Window window = getDialog().getWindow();
            if (getConfig().getWindowBackground() > 0) {
                window.setBackgroundDrawableResource(getConfig().getWindowBackground());
            }

            if (getConfig().isFullScreen()) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(MATCH_PARENT, MATCH_PARENT);
            }
            // 底部弹窗有点问题  会二次绘制  闪动 所以注释(实在需要再开启)
            if (getConfig().isNeedDimension()) {
                int[] dimension = getConfig().getDialogDimension();
                if (getConfig().isWrapDialogContent()) {
                    //dimension[0] = WRAP_CONTENT;
                }
                window.setLayout(dimension[0], dimension[1]);
            }
        }
        getDiyLifecycle().onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getDiyLifecycle().onStop(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean visible = getUserVisibleHint();
        presenterDelegate.onViewResume(visible);
        getDiyLifecycle().onResume(this, visible);
    }

    @Override
    public boolean getUserVisibleHint() {
        boolean visible = super.getUserVisibleHint();
        if (groupFragment != null) {
            visible = visible && groupFragment.getUserVisibleHint();
        }
        return visible;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onViewPause();
        getDiyLifecycle().onPause(this, getUserVisibleHint());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterDelegate.detachView();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onViewDestroy(this);
        getDiyLifecycle().onDestroy(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(USER_VISIBLE, setUserVisibleHintWasCalled);
        super.onSaveInstanceState(outState);
        presenterDelegate.onSaveState(outState);
        getDiyLifecycle().onSaveState(this, outState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean oldUserVisible = userVisible;
        userVisible = isVisibleToUser;
        if (groupFragment != null) {
            userVisible = userVisible && groupFragment.getUserVisibleHint();
        }
        // TODO 传 isVisibleToUser 还是 userVisible ？
        super.setUserVisibleHint(isVisibleToUser);
        // 忽略 setUserVisibleHint 第一次调用，第一次调用往往来自 PagerAdapter#instantiateItem() 方法。
        if (!setUserVisibleHintWasCalled) {
            setUserVisibleHintWasCalled = true;
            return;
        }
        presenterDelegate.setUserVisibleHint(isVisibleToUser);
        if (oldUserVisible != userVisible) {
            getDiyLifecycle().onUserVisibleChange(this, isVisibleToUser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        passiveDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    public P createPresenter() {
        return presenterDelegate.createPresenter();
    }

    @Override
    public Router getRouter() {
        return lifecycle.getRouter();
    }

    /**
     * 获取视图配置。
     *
     * @return 视图配置
     */
    public PassiveConfig getConfig() {
        return passiveDelegate.getConfig();
    }

    @Override
    public void onNavigationClick() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        finish();
    }

    /**
     * 结束当前屏幕。
     */
    protected void finish() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void setTitle(String title) {
        passiveDelegate.setToolbarTitle(title);
    }

    @Override
    public void setTitle(int title) {
        passiveDelegate.setToolbarTitle(title);
    }

    @Override
    public void setToolbarResource(@DrawableRes int resId) {
        passiveDelegate.setToolbarResource(resId);
    }

    @Override
    public void setToolbarColor(@ColorInt int colorId) {
        passiveDelegate.setToolbarColor(colorId);
    }

    @Deprecated
    @Override
    public int getAppbarColor() {
        return passiveDelegate.getStatusBarColor();
    }

    @Override
    public void setAppbarVisible(int visible) {
        passiveDelegate.setToolbarVisible(visible);
    }

    protected PassiveDelegate<P> getPassiveDelegate() {
        return passiveDelegate;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    /**
     * 确保用户界面参数列表。
     *
     * @return 用户界面参数列表
     */
    private Bundle ensureArguments() {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
            setArguments(args);
        }
        return args;
    }

    /**
     * 设置用户界面的整型参数。
     *
     * @param name  参数名称
     * @param value 参数值
     * @return this
     */
    public PassiveFragment setArgument(String name, int value) {
        Bundle args = ensureArguments();
        args.putInt(name, value);
        return this;
    }

    /**
     * 设置用户界面的长整型参数。
     *
     * @param name  参数名称
     * @param value 参数值
     * @return this
     */
    public PassiveFragment setArgument(String name, long value) {
        Bundle args = ensureArguments();
        args.putLong(name, value);
        return this;
    }

    /**
     * 设置用户界面的布尔型参数。
     *
     * @param name  参数名称
     * @param value 参数值
     * @return this
     */
    public PassiveFragment setArgument(String name, boolean value) {
        Bundle args = ensureArguments();
        args.putBoolean(name, value);
        return this;
    }

    /**
     * 设置界面的列表类型参数。
     *
     * @param key  参数名称
     * @param list 列表
     * @return this
     */
    public PassiveFragment setArgument(String key, ArrayList<? extends Parcelable> list) {
        Bundle args = ensureArguments();
        args.putParcelableArrayList(key, list);
        return this;
    }

    /**
     * 设置界面的列表类型参数。
     *
     * @param key          参数名称
     * @param serializable 参数值
     * @return this
     */
    public PassiveFragment setArgument(String key, Serializable serializable) {
        Bundle args = ensureArguments();
        args.putSerializable(key, serializable);
        return this;
    }

    /**
     * 设置用户界面的字符串参数。
     *
     * @param name  参数名称
     * @param value 参数值
     * @return this
     */
    public PassiveFragment<P> setArgument(String name, String value) {
        Bundle args = ensureArguments();
        args.putString(name, value);
        return this;
    }

    /**
     * 设置用户界面的 URI 参数。
     *
     * @param name  参数名称
     * @param value 参数值
     * @return this
     */
    public PassiveFragment setArgument(String name, Parcelable value) {
        Bundle args = ensureArguments();
        args.putParcelable(name, value);
        return this;
    }

    /**
     * 获取用户界面参数值。
     *
     * @param key 参数名称
     * @param <T> 参数类型
     * @return 参数值
     */
    public <T> T getArgument(String key) {
        return getArgument(key, null);
    }

    /**
     * 获取用户界面参数值。
     *
     * @param key          参数名称
     * @param defaultValue 参数默认值
     * @param <T>          参数类型
     * @return 参数值
     */
    @SuppressWarnings("unchecked")
    protected <T> T getArgument(String key, T defaultValue) {
        if (getArguments() == null) {
            return defaultValue;
        }
        Object value = getArguments().get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void setGroupFragment(Fragment groupFragment) {
        this.groupFragment = groupFragment;
    }

    public Fragment getGroupFragment() {
        return groupFragment;
    }
}
