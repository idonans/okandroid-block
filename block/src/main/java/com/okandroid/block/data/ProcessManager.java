package com.okandroid.block.data;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.okandroid.block.lang.Singleton;
import com.okandroid.block.util.ContextUtil;

import java.util.List;

import timber.log.Timber;

/**
 * 记录进程信息，在 app 中可能存在多个进程，在处理如缓存路径时进程之间的应当不同，否则可能出现读写冲突。
 */
public class ProcessManager {

    private static final Singleton<ProcessManager> sInstance =
            new Singleton<ProcessManager>() {
                @Override
                protected ProcessManager create() {
                    return new ProcessManager();
                }
            };

    private static boolean sInit;

    public static ProcessManager getInstance() {
        ProcessManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private int mProcessId;
    private String mProcessName;
    private String mProcessTag;
    private static final String PROCESS_TAG_MAIN = "main";

    private ProcessManager() {
        Timber.v("init");
        mProcessId = android.os.Process.myPid();

        ActivityManager activityManager =
                (ActivityManager)
                        ContextUtil.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos =
                activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.pid == mProcessId) {
                mProcessName = processInfo.processName;
                break;
            }
        }
        if (TextUtils.isEmpty(mProcessName)) {
            throw new IllegalAccessError("process name not found");
        }

        String processName = mProcessName;
        int index = processName.lastIndexOf(':');
        String processSuffix = null;
        if (index >= 0) {
            if (index == 0 || index == processName.length() - 1) {
                throw new IllegalArgumentException("invalid process name " + processName);
            }
            processSuffix = processName.substring(index + 1);
        }

        if (TextUtils.isEmpty(processSuffix)) {
            mProcessTag = PROCESS_TAG_MAIN;
        } else {
            mProcessTag = "sub_" + processSuffix;
        }

        Timber.d("process tag:%s, id:%s, name:%s", mProcessTag, mProcessId, mProcessName);
    }

    public int getProcessId() {
        return mProcessId;
    }

    /**
     * 获取当前进程名称
     */
    public String getProcessName() {
        return mProcessName;
    }

    /**
     * 获取当前进程的标识，可以用于文件名
     */
    public String getProcessTag() {
        return mProcessTag;
    }

    /**
     * 判断当前进程是否为主进程， 主进程的进程名等于包名
     */
    public boolean isMainProcess() {
        return PROCESS_TAG_MAIN.equals(mProcessTag);
    }
}
