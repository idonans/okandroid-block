package com.okandroid.block.data;

import android.support.annotation.Nullable;

import com.okandroid.block.Constants;
import com.okandroid.block.lang.Singleton;
import com.okandroid.block.thread.TaskQueue;
import com.okandroid.block.thread.Threads;
import com.okandroid.block.util.FileUtil;
import com.okandroid.block.util.TimeUtil;

import java.io.File;

import timber.log.Timber;

/**
 * 临时文件管理器, 对于过期的临时文件会自动删除
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

    private static final long MAX_AGE = 2 * TimeUtil.MS_DAY;
    private static final String TMP_DIR = Constants.RESOURCE_PREFIX + "_tmp_files";
    private final TaskQueue mClearQueue = new TaskQueue(1);

    private TmpFileManager() {
        Timber.v("init");
        clear();
    }

    @Nullable
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

    /**
     * 清除过期临时文件
     */
    public void clear() {
        final long delay = 10 * TimeUtil.MS_MIN;
        Threads.postUi(new Runnable() {
            @Override
            public void run() {
                Threads.postBackground(new Runnable() {
                    @Override
                    public void run() {
                        if (mClearQueue.getWaitCount() > 1) {
                            Timber.d("has other task for clear, ignore this.");
                            return;
                        }

                        mClearQueue.enqueue(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clearInternal();
                                    Timber.d("clear success");
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                    Timber.e("exception happen dur clear, retry later");
                                    clear();
                                }
                            }
                        });
                    }
                });
            }
        }, delay);
    }

    /**
     * 清除过期临时文件
     */
    private void clearInternal() throws Throwable {
        File tmpFileDir = getTmpFileDir();
        if (tmpFileDir == null || !tmpFileDir.exists()) {
            Timber.v("clear tmp file dir not found %s", tmpFileDir);
            return;
        }

        File[] files = tmpFileDir.listFiles();
        if (files == null) {
            return;
        }

        int failToRemoveSize = 0;
        for (File file : files) {
            if (file == null) {
                continue;
            }

            if (!file.isFile()) {
                Timber.w("tmp file is not a file %s", file.getCanonicalPath());
            }

            if (file.lastModified() + MAX_AGE < System.currentTimeMillis()) {
                Timber.v("remove expire tmp file %s", file);
                if (!FileUtil.deleteFileQuietly(file)) {
                    failToRemoveSize++;
                    Timber.e("fail to remove expire tmp file %s", file);
                }
            }
        }

        if (failToRemoveSize > 0) {
            throw new IllegalStateException("fail to remove " + failToRemoveSize + " expire tmp files");
        }
    }

}
