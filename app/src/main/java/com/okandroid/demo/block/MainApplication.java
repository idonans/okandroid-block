package com.okandroid.demo.block;

import android.app.Application;
import android.content.Intent;

import com.okandroid.block.AppInit;
import com.okandroid.block.core.StorageManager;
import com.okandroid.block.thread.Threads;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppInit.init(this);

        setupLeakCanary();

        startService(new Intent(this, TaskService.class));

        // DEBUG
        Threads.postBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        StorageManager.getInstance().printAllRows(StorageManager.NAMESPACE_CACHE);
                        StorageManager.getInstance().printAllRows(StorageManager.NAMESPACE_SETTING);
                    }
                });
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
}
