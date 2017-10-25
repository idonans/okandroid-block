package com.okandroid.demo.block;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by idonans on 2017/10/25.
 */

public class MainApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    setupLeakCanary();
  }

  protected RefWatcher setupLeakCanary() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return RefWatcher.DISABLED;
    }
    return LeakCanary.install(this);
  }
}
