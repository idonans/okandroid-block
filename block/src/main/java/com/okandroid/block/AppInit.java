package com.okandroid.block;

import android.content.Context;
import android.content.ContextWrapper;
import com.okandroid.block.util.ContextUtil;

public class AppInit {

  private AppInit() {
  }

  private static boolean sInit;

  public synchronized static void init(Context context) {
    if (sInit) {
      return;
    }

    if (context == null) {
      return;
    }

    if (context instanceof ContextWrapper) {
      context = ((ContextWrapper) context).getBaseContext();

      if (context == null) {
        throw new IllegalStateException("context is null");
      }
    }

    ContextUtil.setContext(context);
    AppEnv.init();
    sInit = true;
  }
}
