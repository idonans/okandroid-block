package com.okandroid.block.util;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class ContextUtil {

    private static Context sContext;

    private ContextUtil() {
    }

    @NonNull
    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalAccessError(
                    "context not found, need call AppInit.init(Context) on application start");
        }
        return sContext;
    }

    /**
     * do not call this method direct, and use AppInit.init(Context) instead.
     */
    public static synchronized void setContext(@NonNull Context context) {
        if (sContext != null) {
            throw new IllegalAccessError("context already set");
        }

        Context originalContext = context;

        context = context.getApplicationContext();

        if (!(context instanceof Application)) {
            throw new IllegalArgumentException("application not found " + originalContext);
        }
        sContext = context;
    }
}
