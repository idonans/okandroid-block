package com.okandroid.demo.block;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.okandroid.block.widget.FixWebView;

/**
 * Created by idonans on 2017/11/9.
 */

public class BrowserActivity extends AppCompatActivity {

  public static Intent startIntent(Context context) {
    Intent starter = new Intent(context, BrowserActivity.class);
    return starter;
  }

  @BindView(R.id.title) TextView mTitle;
  @BindView(R.id.webview) FixWebView mWebView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    {
      View decorView = getWindow().getDecorView();
      decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_browser);
    ButterKnife.bind(this);

    mWebView.loadUrl("http://www.baidu.com");
  }

  @Override public void onBackPressed() {
    if (mWebView.dispatchBackPressed()) {
      return;
    }

    super.onBackPressed();
  }
}
