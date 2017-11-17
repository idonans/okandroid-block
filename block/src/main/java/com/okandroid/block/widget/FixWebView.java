package com.okandroid.block.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.okandroid.block.data.CookiesManager;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;

/**
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

  private CustomViewer mCustomViewer;

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

    setWebViewClient(new WebViewClientImpl());
    setWebChromeClient(new WebChromeClientImpl());
  }

  public class WebViewClientImpl extends WebViewClient {

    protected final String CLASS_NAME = ClassName.valueOf(this);

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

  public class WebChromeClientImpl extends WebChromeClient {

    protected final String CLASS_NAME = ClassName.valueOf(this);

    public WebChromeClientImpl() {
    }

    @Override public void onReceivedTitle(WebView view, String title) {
      Log.v(CLASS_NAME, "onReceivedTitle", title);
    }

    @Override public void onShowCustomView(View view, CustomViewCallback callback) {
      Log.v(CLASS_NAME, "onShowCustomView");
      if (mCustomViewer != null) {
        mCustomViewer.show(view, callback);
      }
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
      Log.v(CLASS_NAME, "onShowCustomView requestedOrientation", requestedOrientation);
      onShowCustomView(view, callback);
    }

    @Override public void onHideCustomView() {
      Log.v(CLASS_NAME, "onHideCustomView");
      if (mCustomViewer != null) {
        mCustomViewer.hide();
      }
    }

    @Override public void onGeolocationPermissionsShowPrompt(String origin,
        GeolocationPermissions.Callback callback) {
      callback.invoke(origin, true, true);
      super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    try {
      if (mCustomViewer != null) {
        mCustomViewer.hide();
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public boolean dispatchBackPressed() {
    if (canGoBack()) {
      goBack();
      return true;
    }
    return false;
  }

  public void setCustomViewer(CustomViewer customViewer) {
    if (mCustomViewer != null) {
      throw new IllegalAccessError("already set custom viewer");
    }
    mCustomViewer = customViewer;
  }

  public static class CustomViewer {

    private final String CLASS_NAME = ClassName.valueOf(this);

    private final Activity mActivity;
    private final ViewGroup mParent;
    private final boolean mIgnoreFullscreen;

    private View mView;
    private WebChromeClient.CustomViewCallback mCallback;

    public CustomViewer(Activity activity, ViewGroup parent) {
      this(activity, parent, false);
    }

    public CustomViewer(Activity activity, ViewGroup parent, boolean ignoreFullscreen) {
      mActivity = activity;
      mParent = parent;
      mIgnoreFullscreen = ignoreFullscreen;
    }

    public void show(View view, WebChromeClient.CustomViewCallback callback) {
      if (view == null) {
        Log.e(CLASS_NAME, "view is null");
        return;
      }

      if (mView != null) {
        Log.e(CLASS_NAME, "already exist custom view", mView);
        return;
      }

      mView = createDecorView(view);
      mCallback = callback;

      mParent.addView(mView);
      if (!mIgnoreFullscreen) {
        requestFullscreen();
      }
    }

    public View createDecorView(View view) {
      FrameLayout decorView = new FrameLayout(view.getContext());
      decorView.setBackgroundColor(Color.BLACK);
      ViewGroup.LayoutParams decorViewLayoutParams =
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      decorView.setLayoutParams(decorViewLayoutParams);
      decorView.addView(view);
      return decorView;
    }

    public void hide() {
      if (mView == null) {
        return;
      }

      mParent.removeView(mView);
      mView = null;
      if (mCallback != null) {
        mCallback.onCustomViewHidden();
      }
      if (!mIgnoreFullscreen) {
        requestExitFullscreen();
      }
    }

    public void requestFullscreen() {
      mActivity.getWindow()
          .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
      mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    public void requestExitFullscreen() {
      mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }
  }
}
