package com.okandroid.block;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import com.okandroid.block.lang.BlockFileProvider;
import com.okandroid.block.util.ContextUtil;

/**
 * call {@link AppInit#init(Context)} on {@link Application#onCreate()},
 * {@link ContentProvider#attachInfo(Context, ProviderInfo)}
 *
 * @see BlockFileProvider#attachInfo(Context, ProviderInfo)
 * @see BlockApplication#onCreate()
 */
public class AppInit {

  private AppInit() {
  }

  private static final boolean DEBUG = true;
  private static boolean sInit;

  public synchronized static void init(Context context) {
    if (sInit) {
      return;
    }
    sInit = true;

    if (DEBUG) {
      new RuntimeException("(DEBUG) AppInit stack").printStackTrace();
    }

    if (context == null) {
      throw new IllegalArgumentException("context is null");
    }

    ContextUtil.setContext(context);
    AppEnvironment.init();
    LocalDataInit.touch();
  }
}
