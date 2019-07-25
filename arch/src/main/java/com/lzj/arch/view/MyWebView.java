package com.lzj.arch.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

public class MyWebView extends WebView {

    private IScrollListener mScrollListener;

    public MyWebView(final Context context) {
        super(context);
    }

    public MyWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollListener != null)
        {
            mScrollListener.onScrollChanged(t);
        }
    }

    public void setScrollListener(IScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }


    public interface IScrollListener
    {
        void onScrollChanged(int scrollY);
    }
}  