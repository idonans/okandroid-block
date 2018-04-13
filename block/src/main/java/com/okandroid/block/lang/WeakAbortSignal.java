package com.okandroid.block.lang;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public class WeakAbortSignal extends SimpleAbortSignal {

    private final WeakReference<AbortSignal> mWeakReference;

    public WeakAbortSignal(@NonNull AbortSignal abortSignal) {
        if (abortSignal.isAbort()) {
            abort();
        }

        mWeakReference = new WeakReference<>(abortSignal);
    }

    @Override
    public boolean isAbort() {
        if (super.isAbort()) {
            return true;
        }

        AbortSignal abortSignal = mWeakReference.get();
        if (abortSignal == null) {
            // 引用的对象被回收，则 abort
            return true;
        }

        return abortSignal.isAbort();
    }

}
