package com.okandroid.block.data;

import android.text.TextUtils;

import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.lang.Singleton;

import java.util.UUID;

/** 在 app 运行期间的唯一标识，即使程序重启，也不会丢失。该标识在程序第一次运行时初始化。 支持跨进程. */
public class AppIDManager {

    private static final Singleton<AppIDManager> sInstance =
            new Singleton<AppIDManager>() {
                @Override
                protected AppIDManager create() {
                    return new AppIDManager();
                }
            };

    private static boolean sInit;

    public static AppIDManager getInstance() {
        AppIDManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private final String CLASS_NAME = ClassName.valueOf(this);
    private static final String KEY_APP_ID = "_app_id";
    private String mAppID;

    private AppIDManager() {
        Log.v(CLASS_NAME, "init");
        mAppID = StorageManager.getInstance().getSetting(KEY_APP_ID);
        if (TextUtils.isEmpty(mAppID)) {
            mAppID = UUID.randomUUID().toString();
            StorageManager.getInstance().setSetting(KEY_APP_ID, mAppID);
        }

        Log.d(CLASS_NAME, "AppID:", mAppID);
    }

    public String getAppID() {
        return mAppID;
    }
}
