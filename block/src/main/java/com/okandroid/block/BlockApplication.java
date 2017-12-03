package com.okandroid.block;

import android.app.Application;

/** Created by idonans on 2017/11/6. */
public class BlockApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this);
    }
}
