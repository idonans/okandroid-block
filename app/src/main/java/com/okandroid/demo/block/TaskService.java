package com.okandroid.demo.block;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;

/** Created by idonans on 2017/11/9. */
public class TaskService extends Service {

    private final String CLASS_NAME = ClassName.valueOf(this);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(CLASS_NAME, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
