package com.okandroid.block;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;

import com.okandroid.block.lang.BlockFileProvider;
import com.okandroid.block.util.ContextUtil;
import com.okandroid.block.util.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * call {@link AppInit#init(Context)} on {@link Application#onCreate()}, {@link
 * ContentProvider#onCreate()}
 *
 * @see BlockFileProvider#onCreate()
 * @see BlockApplication#onCreate()
 */
public class AppInit {

    private AppInit() {}

    private static boolean sInit;

    public static synchronized void init(Context context) {
        if (sInit) {
            return;
        }
        sInit = true;

        final Throwable appInitStackInfo = new Throwable("AppInit#init");
        DelayLog.v(
                new Object() {
                    @Override
                    public String toString() {
                        // delay print stack info
                        ByteArrayOutputStream os = null;
                        PrintWriter pw = null;
                        try {
                            os = new ByteArrayOutputStream(1024 * 8);
                            pw = new PrintWriter(os);
                            pw.write("AppInit stack info\n");
                            appInitStackInfo.printStackTrace(pw);
                            pw.flush();
                            return os.toString();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        } finally {
                            IOUtil.closeQuietly(pw);
                            IOUtil.closeQuietly(os);
                        }
                        return null;
                    }
                });

        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        ContextUtil.setContext(context);
        AppEnvironment.init();

        LocalDataInit.touch();
    }
}
