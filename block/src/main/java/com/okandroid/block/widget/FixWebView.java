package com.okandroid.block.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.okandroid.block.data.CookiesManager;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;

/**
 * Created by idonans on 2017/10/26.
 */

public class FixWebView extends WebView {

  protected final String CLASS_NAME = ClassName.valueOf(this);

  public FixWebView(Context context) {
    super(context);
    init();
  }

  public FixWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FixWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public FixWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  @SuppressWarnings("deprecation")
  public FixWebView(Context context, AttributeSet attrs, int defStyleAttr,
      boolean privateBrowsing) {
    super(context, attrs, defStyleAttr, privateBrowsing);
    init();
  }

  private void init() {
    CookiesManager.getInstance().enableCookie(this);

    WebSettings settings = getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    settings.setAllowFileAccess(true);
    settings.setAppCacheEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setSupportZoom(true);
    settings.setUserAgentString(settings.getUserAgentString() + " Block/0.1");
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    settings.setPluginState(WebSettings.PluginState.ON);
    settings.setLoadsImagesAutomatically(true);
    settings.setGeolocationEnabled(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    setWebViewClient(new WebViewClientImpl(this));
    setWebChromeClient(new WebChromeClientImpl(this));
  }

  public static class WebViewClientImpl extends WebViewClient {

    protected final String CLASS_NAME = ClassName.valueOf(this);
    public final WebView mWebView;

    public WebViewClientImpl(WebView webView) {
      mWebView = webView;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      handler.proceed();
    }

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Log.v(CLASS_NAME, "shouldOverrideUrlLoading", url);
      return false;
    }

    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
      Log.v(CLASS_NAME, "onPageStarted", url, "webview url", view.getUrl());
    }

    @Override public void onPageCommitVisible(WebView view, String url) {
      Log.v(CLASS_NAME, "onPageCommitVisible", url);
    }

    @Override public void onPageFinished(WebView view, String url) {
      Log.v(CLASS_NAME, "onPageFinished", url, "webview url", view.getUrl());
    }
  }

  public static class WebChromeClientImpl extends WebChromeClient {

    protected final String CLASS_NAME = ClassName.valueOf(this);
    public final WebView mWebView;

    public WebChromeClientImpl(WebView webView) {
      mWebView = webView;
    }

    @Override public void onReceivedTitle(WebView view, String title) {
      Log.v(CLASS_NAME, "onReceivedTitle", title);
    }

    @Override public void onShowCustomView(View view, CustomViewCallback callback) {
      Log.v(CLASS_NAME, "onShowCustomView");
      // TODO
      // mCustomViewer.showCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
      Log.v(CLASS_NAME, "onShowCustomView requestedOrientation", requestedOrientation);
      // TODO
      // mCustomViewer.showCustomView(view, callback);
    }

    @Override public void onHideCustomView() {
      Log.v(CLASS_NAME, "onHideCustomView");
      // TODO
      // mCustomViewer.hideLastCustomView();
    }

    @Override public void onGeolocationPermissionsShowPrompt(String origin,
        GeolocationPermissions.Callback callback) {
      callback.invoke(origin, true, true);
      super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
  }

  public boolean dispatchBackPressed() {
    if (canGoBack()) {
      goBack();
      return true;
    }
    return false;
  }
}
