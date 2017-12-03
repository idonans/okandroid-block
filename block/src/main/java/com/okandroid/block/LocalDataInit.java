package com.okandroid.block;

import com.okandroid.block.data.ActivityLifecycleManager;
import com.okandroid.block.data.AppIDManager;
import com.okandroid.block.data.CookiesManager;
import com.okandroid.block.data.FrescoManager;
import com.okandroid.block.data.OkHttpManager;
import com.okandroid.block.data.ProcessManager;
import com.okandroid.block.data.StorageManager;
import com.okandroid.block.data.TmpFileManager;
import com.okandroid.block.thread.Threads;

public class LocalDataInit {

    public static void touch() {
        touchWithBlock();

        // 确保至少延迟到下一个 ui 循环之后
        Threads.postBackgroundAfterLooper(
                new Runnable() {
                    @Override
                    public void run() {
                        touchOnBackground();
                    }
                });
    }

    private static void touchWithBlock() {
        ActivityLifecycleManager.getInstance();

        if (AppEnvironment.getAppProperties().isFrescoEnable()) {
            FrescoManager.getInstance();
        }
    }

    private static void touchOnBackground() {
        AppIDManager.getInstance();
        CookiesManager.getInstance();
        OkHttpManager.getInstance();
        ProcessManager.getInstance();
        StorageManager.getInstance();
        TmpFileManager.getInstance();
    }
}
