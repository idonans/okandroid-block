package com.okandroid.demo.block;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.okandroid.block.core.CookieStoreManager;
import com.okandroid.block.core.StorageManager;

import java.util.List;

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

        if (intent.getBooleanExtra("read_cookie", false)) {
            readCookie();
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

    private void readCookie() {
        List<String> urls = CookieStoreManager.getInstance().getUrls();
        if (urls != null) {
            for (String url : urls) {
                List<String> setCookies = CookieStoreManager.getInstance().get(url);
                if (setCookies != null) {
                    for (String setCookie : setCookies) {
                        Timber.d("found set cookie [%s] for url [%s]", setCookie, url);
                    }
                }

                List<String> matchCookies = CookieStoreManager.getInstance().matches(url);
                if (matchCookies != null) {
                    for (String cookie : matchCookies) {
                        Timber.d("found match cookie [%s] for url [%s]", cookie, url);
                    }
                }
            }
        }

        CookieStoreManager.getInstance().printAll();
    }

}
