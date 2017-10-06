package com.okandroid.block;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import com.okandroid.block.data.FrescoManager;
import com.okandroid.block.lang.Log;
import com.okandroid.block.util.ContextUtil;
import java.util.HashMap;
import java.util.Map;

public class AppEnv {

  private static final String TAG = "AppEnv";

  private static String sLogTag;
  private static String sLogLevel;
  private static int sLogLevelInt;

  private static String sSubDirName;

  private static boolean sFrescoEnable;
  private static boolean sFresco565Config;

  AppEnv() {
  }

  static void init() {
    initLogTag();
    initLogLevel();

    Log.v(TAG, "log tag", getLogTag(), "log level", getLogLevel(), getLogLevelInt());

    initSubDirName();

    Context context = ContextUtil.getContext();
    Resources resources = context.getResources();
    sFrescoEnable = resources.getBoolean(R.bool.okandroid_block_fresco_enable);
    sFresco565Config = resources.getBoolean(R.bool.okandroid_block_fresco_565_config);
    Log.v(TAG, "fresco enable", isFrescoEnable(), "565 config", isFresco565Config());

    initLocalData();
  }

  private static void initLogTag() {
    Context context = ContextUtil.getContext();
    String logTag = context.getResources().getString(R.string.okandroid_block_log_tag);
    if (TextUtils.isEmpty(logTag)) {
      logTag = loadAppLabel();
    }

    if (TextUtils.isEmpty(logTag)) {
      throw new IllegalStateException("log tag is empty");
    }

    sLogTag = logTag;
    Log.setLogTag(logTag);
  }

  private static String loadAppLabel() {
    Context context = ContextUtil.getContext();
    PackageManager pm = context.getPackageManager();
    ApplicationInfo appInfo = context.getApplicationInfo();
    return String.valueOf(appInfo.loadLabel(pm));
  }

  private static void initLogLevel() {
    Context context = ContextUtil.getContext();
    String logLevel = context.getResources().getString(R.string.okandroid_block_log_level);
    if (TextUtils.isEmpty(logLevel)) {
      logLevel = "WARN";
    }

    logLevel = logLevel.toUpperCase();

    Map<String, Integer> level = new HashMap<>(5);
    level.put("VERBOSE", android.util.Log.VERBOSE);
    level.put("DEBUG", android.util.Log.DEBUG);
    level.put("INFO", android.util.Log.INFO);
    level.put("WARN", android.util.Log.WARN);
    level.put("ERROR", android.util.Log.ERROR);

    Integer logLevelInt = level.get(logLevel);
    if (logLevelInt == null) {
      throw new IllegalStateException(
          "error log level[" + logLevel + "], only VERBOSE DEBUG INFO WARN ERROR support");
    }

    sLogLevel = logLevel;
    sLogLevelInt = logLevelInt;
    Log.setLogLevel(logLevelInt);
  }

  private static void initSubDirName() {
    Context context = ContextUtil.getContext();
    String subDirName = context.getResources().getString(R.string.okandroid_block_sub_dir_name);
    if (TextUtils.isEmpty(subDirName)) {
      subDirName = loadAppLabel();
    }

    if (TextUtils.isEmpty(subDirName)) {
      throw new IllegalStateException("sub dir name is empty");
    }

    Log.v(TAG, "sub dir name", subDirName);
    sSubDirName = subDirName;
  }

  private static void initLocalData() {
    if (sFrescoEnable) {
      FrescoManager.getInstance();
    }
  }

  public static String getLogTag() {
    return sLogTag;
  }

  public static String getLogLevel() {
    return sLogLevel;
  }

  public static int getLogLevelInt() {
    return sLogLevelInt;
  }

  public static String getSubDirName() {
    return sSubDirName;
  }

  public static boolean isFrescoEnable() {
    return sFrescoEnable;
  }

  public static boolean isFresco565Config() {
    return sFresco565Config;
  }
}
