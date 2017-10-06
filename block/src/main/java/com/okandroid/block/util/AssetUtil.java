package com.okandroid.block.util;

import android.content.Context;
import android.content.res.AssetManager;
import com.okandroid.block.lang.Available;
import com.okandroid.block.lang.Progress;
import java.io.InputStream;
import java.util.List;

/**
 * helper for read asset content
 */
public class AssetUtil {

  public static String readAllAsString(String path, Available available, Progress progress)
      throws Throwable {
    Context context = ContextUtil.getContext();
    AssetManager assetManager = context.getAssets();
    InputStream is = null;
    try {
      is = assetManager.open(path);
      return IOUtil.readAllAsString(is, available, progress);
    } finally {
      IOUtil.closeQuietly(is);
    }
  }

  public static byte[] readAll(String path, Available available, Progress progress)
      throws Throwable {
    Context context = ContextUtil.getContext();
    AssetManager assetManager = context.getAssets();
    InputStream is = null;
    try {
      is = assetManager.open(path);
      return IOUtil.readAll(is, available, progress);
    } finally {
      IOUtil.closeQuietly(is);
    }
  }

  public static List<String> readAllLines(String path, Available available, Progress progress)
      throws Throwable {
    Context context = ContextUtil.getContext();
    AssetManager assetManager = context.getAssets();
    InputStream is = null;
    try {
      is = assetManager.open(path);
      return IOUtil.readAllLines(is, available, progress);
    } finally {
      IOUtil.closeQuietly(is);
    }
  }
}
