package com.okandroid.block.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.lang.StorageDatabaseProvider;
import com.okandroid.block.util.ContextUtil;
import com.okandroid.block.util.IOUtil;

/**
 * 数据存储服务, 对接 {@link StorageDatabaseProvider}. 支持跨进程
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

  private StorageManager() {
    Log.v(CLASS_NAME, "init");
  }

  public void setSetting(@Nullable String key, @Nullable String value) {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getSettingUri(context);

    ContentValues values = new ContentValues(2);
    values.put(StorageDatabaseProvider.COLUMN_KEY, key);
    values.put(StorageDatabaseProvider.COLUMN_VALUE, value);

    contentResolver.insert(uri, values);
  }

  @CheckResult public String getSetting(@Nullable final String key) {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getSettingUri(context);

    Cursor cursor = contentResolver.query(uri, null, null, new String[] { key }, null);
    try {
      if (cursor != null) {
        if (cursor.moveToFirst()) {
          int indexValue = cursor.getColumnIndex(StorageDatabaseProvider.COLUMN_VALUE);
          if (indexValue >= 0) {
            return cursor.getString(indexValue);
          }
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(cursor);
    }

    return null;
  }

  public void setCache(@Nullable String key, @Nullable String value) {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getCacheUri(context);

    ContentValues values = new ContentValues(2);
    values.put(StorageDatabaseProvider.COLUMN_KEY, key);
    values.put(StorageDatabaseProvider.COLUMN_VALUE, value);

    contentResolver.insert(uri, values);
  }

  @CheckResult public String getCache(@Nullable final String key) {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getCacheUri(context);

    Cursor cursor = contentResolver.query(uri, null, null, new String[] { key }, null);
    try {
      if (cursor != null) {
        if (cursor.moveToFirst()) {
          int indexValue = cursor.getColumnIndex(StorageDatabaseProvider.COLUMN_VALUE);
          if (indexValue >= 0) {
            return cursor.getString(indexValue);
          }
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(cursor);
    }

    return null;
  }

  /**
   * 打印所有 Cache 内容， 协助调试使用
   */
  public void printCacheContent() {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getCacheUri(context);

    contentResolver.update(uri, new ContentValues(), null, null);
  }

  /**
   * 打印所有 Setting 内容， 协助调试使用
   */
  public void printSettingContent() {
    Context context = ContextUtil.getContext();
    ContentResolver contentResolver = context.getContentResolver();

    Uri uri = StorageDatabaseProvider.getSettingUri(context);

    contentResolver.update(uri, new ContentValues(), null, null);
  }
}

