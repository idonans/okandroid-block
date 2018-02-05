package com.okandroid.block;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.okandroid.block.lang.Log;

/** 用于延迟打印 Log, 在 Log 参数初始化之前调用，可以延迟到 Log 参数完成后再打印 */
class DelayLog {

    // 为了避免循环引用，初始化时避免引用其它类
    private static final Handler sHandlerUi = new Handler(Looper.getMainLooper());

    private DelayLog() {}

    public static void v(@Nullable final Object... messages) {
        sHandlerUi.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.v(messages);
                    }
                });
    }

    public static void d(@Nullable final Object... messages) {
        sHandlerUi.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(messages);
                    }
                });
    }

    public static void e(@Nullable final Object... messages) {
        sHandlerUi.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e(messages);
                    }
                });
    }
}
