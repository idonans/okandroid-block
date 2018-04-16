package com.okandroid.block.util;

import android.support.annotation.Nullable;

import com.okandroid.block.lang.AbortException;
import com.okandroid.block.lang.AbortSignal;

public class AbortUtil {

    /**
     * @param abortSignal
     * @return true if abortSignal is null or isAbort, otherwise false.
     */
    public static boolean isAbort(@Nullable AbortSignal abortSignal) {
        if (abortSignal == null) {
            return true;
        }
        return abortSignal.isAbort();
    }

    /**
     * @see #isAbort(AbortSignal)
     */
    public static void throwIfAbort(@Nullable AbortSignal abortSignal) {
        if (isAbort(abortSignal)) {
            throw new AbortException();
        }
    }

    /**
     * one AbortSingle the method isAbort always return false.
     */
    public static final AbortSignal UNINTERRUPTED = new AbortSignal() {
        @Override
        public boolean isAbort() {
            return false;
        }
    };

}
