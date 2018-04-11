package com.okandroid.block;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;

import com.okandroid.block.lang.BlockFileProvider;
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

    public static synchronized void init(Context context) {
        if (sInit) {
            return;
        }
        sInit = true;

        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        sDebug = context.getResources().getBoolean(R.bool.okandroid_block_debug);

        if (sDebug) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.v(new Throwable());

        ContextUtil.setContext(context);
        AppEnvironment.init();

        LocalDataInit.touch();
    }

    public static boolean isDebug() {
        return sDebug;
    }

}
