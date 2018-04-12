package com.okandroid.demo.block;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import timber.log.Timber;

public class TaskService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
