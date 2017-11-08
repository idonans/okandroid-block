package com.okandroid.block.data;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.okandroid.block.db.SimpleDB;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.thread.Threads;

/**
 * 数据存储服务
 * <br>
 * 不同进程间的数据不同，不能使用此存储实现进程间共享数据。
 * 此处提供了 setting 和 cache 两个不同的存储空间，各自独立运行。
 */
public class StorageManager {

  private static class InstanceHolder {

    private static final StorageManager sInstance = new StorageManager();
  }

  private static boolean sInit;

  public static StorageManager getInstance() {
    StorageManager instance = InstanceHolder.sInstance;
    sInit = true;
    return instance;
  }

  public static boolean isInit() {
    return sInit;
  }

  private final String CLASS_NAME = ClassName.valueOf(this);
  private final SimpleDB mDBSetting;
  private final SimpleDB mDBCache;

  private StorageManager() {
    Log.v(CLASS_NAME, "init");
    mDBSetting = new SimpleDB("setting");
    mDBCache = new SimpleDB("cache");

    // 异步处理
    Threads.postBackground(new Runnable() {
      @Override public void run() {
        // 数据库启动时, 做一次 trim 操作.
        int settingTrimSize = mDBSetting.trim(5000);
        Log.d(CLASS_NAME, "setting trim size:", settingTrimSize);
        int cacheTrimSize = mDBCache.trim(5000);
        Log.d(CLASS_NAME, "cache trim size:" + cacheTrimSize);
      }
    });
  }

  public void setSetting(@Nullable String key, @Nullable String value) {
    mDBSetting.set(key, value);
  }

  @CheckResult public String getSetting(@Nullable final String key) {
    String value = mDBSetting.get(key);
    if (!TextUtils.isEmpty(value)) {
      // 异步处理
      Threads.postBackground(new Runnable() {
        @Override public void run() {
          mDBSetting.touch(key);
        }
      });
    }
    return value;
  }

  public void setCache(@Nullable String key, @Nullable String value) {
    mDBCache.set(key, value);
  }

  @CheckResult public String getCache(@Nullable final String key) {
    String value = mDBCache.get(key);
    if (!TextUtils.isEmpty(value)) {
      // 异步处理
      Threads.postBackground(new Runnable() {
        @Override public void run() {
          mDBCache.touch(key);
        }
      });
    }
    return value;
  }

  /**
   * 打印所有 Cache 内容， 协助调试使用
   */
  public void printCacheContent() {
    mDBCache.printAllRows();
  }

  /**
   * 打印所有 Setting 内容， 协助调试使用
   */
  public void printSettingContent() {
    mDBSetting.printAllRows();
  }
}

