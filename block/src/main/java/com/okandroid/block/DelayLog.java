package com.okandroid.block;

import android.support.annotation.Nullable;

import com.okandroid.block.lang.Log;

import java.util.LinkedList;

/** 用于延迟打印 Log, 在 Log 参数初始化之前调用，可以延迟到 Log 参数完成后再打印 */
class DelayLog {

    // 为了避免循环引用，初始化时避免引用其它类
    private static LinkedList<Runnable> sDelayList = new LinkedList<>();

    private DelayLog() {}

    public static synchronized void v(@Nullable final Object... messages) {
        if (sDelayList != null) {
            sDelayList.add(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.v(messages);
                        }
                    });
        } else {
            Log.v(messages);
        }
    }

    public static synchronized void d(@Nullable final Object... messages) {
        if (sDelayList != null) {
            sDelayList.add(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.d(messages);
                        }
                    });
        } else {
            Log.d(messages);
        }
    }

    public static synchronized void e(@Nullable final Object... messages) {
        if (sDelayList != null) {
            sDelayList.add(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.e(messages);
                        }
                    });
        } else {
            Log.e(messages);
        }
    }

    public static synchronized void printAllDelayLog() {
        LinkedList<Runnable> list = sDelayList;
        sDelayList = null;

        if (list != null) {
            while (!list.isEmpty()) {
                Log.v("[delay log]");
                list.removeFirst().run();
            }
        }
    }
}
