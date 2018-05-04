package com.okandroid.block.data;

import com.okandroid.block.core.StorageManager;
import com.okandroid.block.lang.Singleton;

import java.util.UUID;

import timber.log.Timber;

/**
 * 在 app 运行期间的唯一标识，即使程序重启，也不会丢失。该标识在程序第一次运行时初始化。 支持跨进程.
 */
@Deprecated
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

    private static final String KEY_APP_ID = "_app_id";
    private String mAppID;

    private AppIDManager() {
        Timber.v("init");
        mAppID = StorageManager.getInstance().getOrSetLock(
                StorageManager.NAMESPACE_SETTING,
                KEY_APP_ID,
                UUID.randomUUID().toString());

        Timber.d("AppID=%s", mAppID);
    }

    public String getAppID() {
        return mAppID;
    }
}
