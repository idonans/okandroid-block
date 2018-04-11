package com.okandroid.block.data;

import android.support.annotation.CheckResult;

import com.okandroid.block.lang.Singleton;
import com.okandroid.block.thread.Threads;
import com.okandroid.block.util.FileUtil;

import java.io.File;

import timber.log.Timber;

/**
 * 临时文件管理器, 当 app 启动时(非恢复的情况), 应当调用 clear 方法以清除遗留的临时文件. 通常, 在 Splash#onCreate(savedInstanceState ==
 * null) 时, 调用 #clear().
 */
public class TmpFileManager {

    private static final Singleton<TmpFileManager> sInstance =
            new Singleton<TmpFileManager>() {
                @Override
                protected TmpFileManager create() {
                    return new TmpFileManager();
                }
            };

    private static boolean sInit;

    public static TmpFileManager getInstance() {
        TmpFileManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private static final String TMP_DIR = "okandroid_tmp_file";
    private static final String TMP_DIR_REMOVED = "okandroid_tmp_file_removed";

    private TmpFileManager() {
        Timber.v("init");
    }

    @CheckResult
    public File createNewTmpFileQuietly(String prefix, String suffix) {
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

    /**
     * 清除临时文件
     */
    public void clear() {
        File tmpFileDir = getTmpFileDir();
        if (tmpFileDir == null || !tmpFileDir.exists()) {
            Timber.v("clear tmp file dir not found %s", tmpFileDir);
            return;
        }

        final File tmpFileDirRemoved = getTmpFileDirRemoved();
        if (!FileUtil.createDir(tmpFileDirRemoved)) {
            Timber.e("clear fail to create tmp file dir removed %s", tmpFileDirRemoved);
            return;
        }

        File renameTo = new File(tmpFileDirRemoved, "rename_" + System.currentTimeMillis());
        if (!tmpFileDir.renameTo(renameTo)) {
            Timber.e("clear rename tmp file dir fail %s->%s", tmpFileDir, renameTo);
            return;
        }

        Timber.v("clear rename tmp file dir success %s->%s", tmpFileDir, renameTo);

        // delete async
        Threads.postBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        long timeStart = System.currentTimeMillis();
                        Timber.v(
                                "clear tmp file dir removed in background start %s",
                                tmpFileDirRemoved);
                        if (FileUtil.deleteFileQuietly(tmpFileDirRemoved)) {
                            long timeDur = System.currentTimeMillis() - timeStart;
                            Timber.v(
                                    "clear tmp file dir removed in background success in %sms %s",
                                    timeDur,
                                    tmpFileDirRemoved);
                        } else {
                            Timber.e(
                                    "clear tmp file dir removed in background fail %s",
                                    tmpFileDirRemoved);
                        }
                    }
                });
    }
}
