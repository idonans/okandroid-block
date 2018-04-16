package com.okandroid.block;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.okandroid.block.lang.BlockFileProvider;
import com.okandroid.block.lang.NotInitException;
import com.okandroid.block.util.ContextUtil;

import timber.log.Timber;

/**
 * call {@link AppInit#init(Context)} on {@link Application#onCreate()}, {@link
 * ContentProvider#onCreate()}
 *
 * @see BlockFileProvider#onCreate()
 */
public class AppInit {

    private AppInit() {
    }

    private static boolean sInit;
    private static boolean sDebug;

    public static synchronized void init(@NonNull Context context) {
        if (sInit) {
            return;
        }
        sInit = true;

        // set global context first
        ContextUtil.setContext(context);

        sDebug = isBuildDebug(context);
        if (sDebug) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.v(new Throwable());

        AppEnvironment.init();
        LocalDataInit.touch();
    }

    private static void throwIfNotInit() {
        if (!sInit) {
            throw new NotInitException();
        }
    }

    public static boolean isDebug() {
        throwIfNotInit();
        return sDebug;
    }

    private static boolean isBuildDebug(@NonNull Context context) {
        return context.getResources().getBoolean(R.bool.okandroid_block_debug);
    }

}
