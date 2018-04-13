package com.okandroid.block.util;

import android.support.annotation.Nullable;

import com.okandroid.block.lang.AbortException;
import com.okandroid.block.lang.AbortSignal;

public class AbortUtil {

    public static boolean isAbort(@Nullable AbortSignal abortSignal) {
        if (abortSignal == null) {
            return false;
        }
        return abortSignal.isAbort();
    }

    public static void throwIfAbort(@Nullable AbortSignal abortSignal) {
        if (isAbort(abortSignal)) {
            throw new AbortException();
        }
    }

}
