package com.okandroid.demo.block;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.item_content) TextView mItemContent;
  @BindView(R.id.fullscreen_toggle) TextView mFullscreenToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    {
      View decorView = getWindow().getDecorView();
      decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mItemContent.setText("hello, butter knife");
    RxView.clicks(mFullscreenToggle)
        .throttleFirst(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (mFullscreen) {
              requestExitFullscreen();
            } else {
              requestFullscreen();
            }
          }
        });
  }

  private boolean mFullscreen;

  private void requestFullscreen() {
    mFullscreen = true;

    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
  }

  private void requestExitFullscreen() {
    mFullscreen = false;

    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }
}
