package com.okandroid.block.data;

import android.text.TextUtils;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import java.util.UUID;

/**
 * 在 app 运行期间的唯一标识，即使程序重启，也不会丢失。该标识在程序第一次运行时初始化。
 * 注意：不同的进程间的标识不同，如果用户清除了程序数据，则会在程序下一次运行时重新初始化，
 * 但此种情况下初始化的值与之前的不同。
 */
public class AppIDManager {

  private static class InstanceHolder {
    private static final AppIDManager sInstance = new AppIDManager();
  }

  private static boolean sInit;

  public static AppIDManager getInstance() {
    AppIDManager instance = InstanceHolder.sInstance;
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
    if (!ProcessManager.getInstance().isMainProcess()) {
      Log.e(CLASS_NAME, "AppID:", mAppID,
          "should be call from main process, different process will get different AppID.");
    }
  }

  public String getAppID() {
    return mAppID;
  }
}
