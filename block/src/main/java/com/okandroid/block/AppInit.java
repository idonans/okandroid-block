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
 * @see BlockApplication#attachBaseContext(Context)
 */
public class AppInit {

  private AppInit() {
  }

  private static boolean sInit;

  public synchronized static void init(Context context) {
    if (sInit) {
      return;
    }
    sInit = true;

    if (context == null) {
      throw new IllegalArgumentException("context is null");
    }

    ContextUtil.setContext(context);
    AppEnvironment.init();
    LocalDataInit.touch();
  }
}
