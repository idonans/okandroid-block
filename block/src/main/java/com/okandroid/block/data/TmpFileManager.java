package com.okandroid.block.data;

import android.support.annotation.CheckResult;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.thread.Threads;
import com.okandroid.block.util.FileUtil;
import java.io.File;

/**
 * 临时文件管理器, 当进程重启时, 临时文件会被删除.
 */
public class TmpFileManager {

  private static class InstanceHolder {

    private static final TmpFileManager sInstance = new TmpFileManager();
  }

  private static boolean sInit;

  public static TmpFileManager getInstance() {
    TmpFileManager instance = InstanceHolder.sInstance;
    sInit = true;
    return instance;
  }

  public static boolean isInit() {
    return sInit;
  }

  private final String CLASS_NAME = ClassName.valueOf(this);
  private static final String TMP_DIR = "okandroid_tmp_file";
  private static final String TMP_DIR_REMOVED = "okandroid_tmp_file_removed";

  private TmpFileManager() {
    Log.v(CLASS_NAME, "init");
    clear();
  }

  @CheckResult public File createNewTmpFileQuietly(String prefix, String suffix) {
    return FileUtil.createNewTmpFileQuietly(prefix, suffix, getTmpFileDir());
  }

  private File getTmpFileDir() {
    File extCacheDir = FileUtil.getExternalCacheDir();
    if (extCacheDir == null) {
      return null;
    }

    return new File(FileUtil.getExternalCacheDir(), TMP_DIR);
  }

  private File getTmpFileDirRemoved() {
    File extCacheDir = FileUtil.getExternalCacheDir();
    if (extCacheDir == null) {
      return null;
    }

    return new File(FileUtil.getExternalCacheDir(), TMP_DIR_REMOVED);
  }

  private void clear() {
    File tmpFileDir = getTmpFileDir();
    if (tmpFileDir == null || !tmpFileDir.exists()) {
      Log.v(CLASS_NAME, "clear tmp file dir not found", tmpFileDir);
      return;
    }

    final File tmpFileDirRemoved = getTmpFileDirRemoved();
    if (!FileUtil.createDir(tmpFileDirRemoved)) {
      Log.e(CLASS_NAME, "clear fail to create tmp file dir removed", tmpFileDirRemoved);
      return;
    }

    File renameTo = new File(tmpFileDirRemoved, "rename_" + System.currentTimeMillis());
    if (!tmpFileDir.renameTo(renameTo)) {
      Log.e(CLASS_NAME, "clear rename tmp file dir fail", tmpFileDir, "->", renameTo);
      return;
    }

    Log.v(CLASS_NAME, "clear rename tmp file dir success", tmpFileDir, "->", renameTo);

    // delete async
    Threads.postBackground(new Runnable() {
      @Override public void run() {
        long timeStart = System.currentTimeMillis();
        Log.v(CLASS_NAME, "clear tmp file dir removed in background start", tmpFileDirRemoved);
        if (FileUtil.deleteFileQuietly(tmpFileDirRemoved)) {
          long timeDur = System.currentTimeMillis() - timeStart;
          Log.v(CLASS_NAME, "clear tmp file dir removed in background success in", timeDur, "ms",
              tmpFileDirRemoved);
        } else {
          Log.e(CLASS_NAME, "clear tmp file dir removed in background fail", tmpFileDirRemoved);
        }
      }
    });
  }
}
