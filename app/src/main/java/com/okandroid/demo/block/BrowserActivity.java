package com.okandroid.demo.block;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.okandroid.block.util.IOUtil;
import com.okandroid.block.widget.FixWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowserActivity extends AppCompatActivity {

    public static Intent startIntent(Context context) {
        Intent starter = new Intent(context, BrowserActivity.class);
        return starter;
    }

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.webview)
    FixWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        mWebView.setWebChromeClient(
                new FixWebView.WebChromeClientImpl(mWebView) {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        mTitle.setText(title);
                    }
                });
        mWebView.setCustomViewer(
                new FixWebView.CustomViewer(
                        this, (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT)));
        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.dispatchBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOUtil.closeQuietly(mWebView);
    }
}
