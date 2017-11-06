package com.okandroid.demo.block;

import com.okandroid.block.BlockApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by idonans on 2017/10/25.
 */

public class MainApplication extends BlockApplication {

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
