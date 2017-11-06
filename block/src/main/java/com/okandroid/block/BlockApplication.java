package com.okandroid.block;

import android.app.Application;
import android.content.Context;

/**
 * Created by idonans on 2017/11/6.
 */

public class BlockApplication extends Application {

  @Override protected void attachBaseContext(Context base) {
    AppInit.init(base);
    super.attachBaseContext(base);
  }
}
