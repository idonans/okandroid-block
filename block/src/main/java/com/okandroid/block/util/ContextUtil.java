package com.okandroid.block.util;

import android.content.Context;

public class ContextUtil {

  private static Context sContext;

  private ContextUtil() {
  }

  public static Context getContext() {
    return sContext;
  }

  public static void setContext(Context context) {
    sContext = context;
  }
}