package com.okandroid.block;

import android.app.Application;
import android.app.Service;
import android.content.ContentProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.okandroid.block.lang.BlockFileProvider;
import com.okandroid.block.lang.NotInitException;
import com.okandroid.block.util.ContextUtil;

import timber.log.Timber;

/**
 * call {@link AppInit#init(Context)} on {@link Application#onCreate()}, {@link
 * ContentProvider#onCreate(), {@link Service#onCreate()}}
 *
 * @see BlockFileProvider#onCreate()
 */
public class AppInit {

    private AppInit() {
    }

    private static boolean sInit;
    private static AppCallbacks sAppCallbacks;
    private static String sDefaultUserAgent;

    public static synchronized void init(@NonNull Context context) {
        if (sInit) {
            return;
        }

        // set global context first
        ContextUtil.setContext(context);

        sInit = true;

        if (isDebug()) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.v(new Throwable());

        sAppCallbacks = new AppCallbacks();
    }

    private static void throwIfNotInit() {
        if (!sInit) {
            throw new NotInitException();
        }
    }

    public static boolean isDebug() {
        throwIfNotInit();
        return ContextUtil.getContext().getResources().getBoolean(R.bool.okandroid_block_debug);
    }

    public static String getSubDirName() {
        throwIfNotInit();
        return ContextUtil.getContext().getString(R.string.okandroid_block_sub_dir_name);
    }

    public static long getRemoteTimeoutMs() {
        throwIfNotInit();
        return 20 * 1000; // 20s
    }

    public static AppCallbacks getAppCallbacks() {
        throwIfNotInit();
        return sAppCallbacks;
    }

    public static void setDefaultUserAgent(@Nullable String userAgent) {
        throwIfNotInit();
        sDefaultUserAgent = userAgent;
    }

    @Nullable
    public static String getDefaultUserAgent() {
        throwIfNotInit();
        return sDefaultUserAgent;
    }

}
