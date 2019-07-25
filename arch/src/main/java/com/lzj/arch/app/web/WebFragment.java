/*
 * Copyright (c) 2017 3000.com All Rights Reserved.
 */
package com.lzj.arch.app.web;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lzj.arch.R;
import com.lzj.arch.app.content.ContentFragment;
import com.lzj.arch.app.web.WebContract.PassiveView;
import com.lzj.arch.app.web.WebContract.Presenter;
import com.lzj.arch.util.ChannelUtil;
import com.lzj.arch.view.MyWebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.lzj.arch.util.ViewUtils.setVisible;

/**
 * 网页内容界面。
 *
 * @author 吴吉林
 */
public class WebFragment<P extends Presenter>
        extends ContentFragment<P>
        implements PassiveView {

    /**
     * 是否打开新的页面
     */
    protected boolean newPage;

    {
        getConfig().setLayoutResource(R.layout.app_fragment_web);
    }

    /**
     * 网页视图。
     */
    private MyWebView webView;

    /**
     * 加载进度。
     */
    private ProgressBar progress;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    private final int FILECHOOSER_RESULT_CODE = 10000;

    /**
     * Chrome 客户端。
     */
    private WebChromeClient webChromeClient = new WebChromeClient() {

        private View wholeView;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress.setProgress(newProgress);
        }

        // 处理h5调用全屏方法 部分手机会出问题
        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            parent.removeView(webView);
            parent.addView(view);
            wholeView = view;
        }

        @Override
        public void onHideCustomView() {
            if (wholeView != null) {
                ViewGroup parent = (ViewGroup) wholeView.getParent();
                parent.removeView(wholeView);
                parent.addView(webView);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            getPresenter().onTitleReceived(title);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            WebFragment.this.onShowFileChooser(uploadMsg, acceptType);
        }

        //For Android 4.1
        @Override
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            WebFragment.this.onShowFileChooser(uploadMsg, acceptType);
        }

        // For Android 5.0+
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            String type = "";
            if (fileChooserParams.getAcceptTypes() != null && fileChooserParams.getAcceptTypes().length != 0) {
                String[] types = fileChooserParams.getAcceptTypes();
                type = types[0];
            }
            WebFragment.this.onShowFileChooser5(filePathCallback, type);
            return true;
        }
    };

    @CallSuper
    @Override
    public void onFindView() {
        super.onFindView();
        webView = findView(R.id.web);
        progress = findView(R.id.progress);
    }

    @CallSuper
    @Override
    public void onInitView(Bundle state) {
        super.onInitView(state);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setScrollListener(new MyWebView.IScrollListener() {
            @Override
            public void onScrollChanged(int scrollY) {
                if (scrollY == 0) {
                    setRefreshEnabled(true);
                } else {
                    setRefreshEnabled(false);
                }
            }
        });
        webView.setDownloadListener(new MyWebViewDownloadListener());
        // 不清理
        // webView.clearCache(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        // 允许js弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自动播放音乐
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        onInitWebSettings(settings);
    }

    /**
     * 处理网页设置。
     *
     * @param settings 网页设置
     */
    protected void onInitWebSettings(WebSettings settings) {
        // 空实现
    }

    public void clearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @Override
    public void showLoading() {
        progress.setProgress(0);
        setProgressVisible(true);
    }

    @Override
    public void setProgressVisible(boolean visible) {
        setVisible(progress, visible);
    }

    @Override
    public void load(String url) {
        Map headers = new HashMap();
        headers.put("ditch", ChannelUtil.getChannel(getContext()));
        webView.loadUrl(url, headers);
    }

    @Override
    public void refresh() {
        webView.reload();
    }

    @Override
    public void callback(String callback) {
        webView.loadUrl("javascript:" + callback + "()");
    }

    @Override
    public void callback(final String callback, final Object param) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + callback + "(" + param + ")");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    /**
     * 添加网页接口。  （有待改善 这样导致  只能   是最后一个子类  调用add  不能基类调用）
     *
     * @param webInterface 网页接口
     */
    protected void addWebInterface(WebInterface<P> webInterface) {
        if (webInterface != null) {
            webInterface.setFragment(this);
            webView.addJavascriptInterface(webInterface, webInterface.getName());
        }
    }

    /**
     * 是否覆盖给定 URL 地址的加载。
     *
     * @param view 网页视图
     * @param url  网页 URL 地址
     * @return true：覆盖；false：未覆盖。
     */
    protected boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    /**
     * 重定向 URL 地址。
     *
     * @param view 网页视图
     * @param url  网页 URL 地址
     * @return true：覆盖；false：未覆盖。
     */
    protected WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return null;
    }

    /**
     * 网页客户端。
     */
    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return WebFragment.this.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return WebFragment.this.shouldInterceptRequest(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            getPresenter().onPageStart(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            newPage = true;
            getPresenter().onPageEnd(url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            switch (errorCode) {
                case ERROR_HOST_LOOKUP:
                case ERROR_IO:
                case ERROR_CONNECT:
                case ERROR_UNKNOWN:
                    description = getString(R.string.http_code_no_network);
                    break;
                case ERROR_TIMEOUT:
                    description = getString(R.string.http_code_timeout);
                    break;
            }
            getPresenter().onPageError(errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            new AlertDialog.Builder(webView.getContext())
                    .setTitle("SSL证书错误")
                    .setMessage("SSL错误码: " + sslError.getPrimaryError())
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sslErrorHandler.proceed();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sslErrorHandler.cancel();
                        }
                    })
                    .create().show();
        }
    };

    @Override
    public void onNavigationClick() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onNavigationClick();
        }
    }

    /**
     * webview下载监听
     */
    private class MyWebViewDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String disposition, String mimeType, long length) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    protected void onShowFileChooser(ValueCallback<Uri> uploadMsg, String type) {
        uploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        if (type != null && type.contains("audio")) {
            i.setType("audio/*");
        } else {
            i.setType("image/*");
        }
        startActivityForResult(Intent.createChooser(i, "Chooser"), FILECHOOSER_RESULT_CODE);
    }

    protected void onShowFileChooser5(ValueCallback<Uri[]> uploadMsg, String type) {
        uploadMessageAboveL = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        if (type != null && type.contains("audio")) {
            i.setType("audio/*");
        } else {
            i.setType("image/*");
        }
        startActivityForResult(Intent.createChooser(i, "Chooser"), FILECHOOSER_RESULT_CODE);
    }

    // 3.选择图片后处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILECHOOSER_RESULT_CODE || uploadMessageAboveL == null){
            return;
        }
        Uri[] results = null;
        if (resultCode == RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null){
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
}
