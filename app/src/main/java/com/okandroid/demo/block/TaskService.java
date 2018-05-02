package com.okandroid.demo.block;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.okandroid.block.core.StorageManager;

import timber.log.Timber;

public class TaskService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("read_ipc", false)) {
            readIPC();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void readIPC() {
        String key = "test_ipc_huge_text_3";
        String value = StorageManager.getInstance().get(StorageManager.NAMESPACE_CACHE, key);
        Timber.d("readIPC %s -> %s", key, value);

        StorageManager.getInstance().printAllRows(StorageManager.NAMESPACE_CACHE);
    }

}
