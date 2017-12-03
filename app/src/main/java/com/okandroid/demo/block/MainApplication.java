package com.okandroid.demo.block;

import android.content.Intent;
import com.okandroid.block.BlockApplication;
import com.okandroid.block.data.StorageManager;
import com.okandroid.block.thread.Threads;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/** Created by idonans on 2017/10/25. */
public class MainApplication extends BlockApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setupLeakCanary();

        startService(new Intent(this, TaskService.class));

        // DEBUG
        Threads.postBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        StorageManager.getInstance().printCacheContent();
                        StorageManager.getInstance().printSettingContent();
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
