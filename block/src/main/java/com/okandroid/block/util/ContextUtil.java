package com.okandroid.block.util;

import android.app.Application;
import android.content.Context;

public class ContextUtil {

    private static Context sContext;

    private ContextUtil() {}

    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalAccessError(
                    "context not found, need call AppInit.init(Context) on application start");
        }
        return sContext;
    }

    /** do not call this method direct, and use AppInit.init(Context) instead. */
    public static void setContext(Context context) {
        if (sContext == null) {
            Context originalContext = context;

            context = context.getApplicationContext();

            if (!(context instanceof Application)) {
                throw new IllegalArgumentException("application not found " + originalContext);
            }
            sContext = context;
        } else {
            new IllegalStateException("already set context").printStackTrace();
        }
    }
}
