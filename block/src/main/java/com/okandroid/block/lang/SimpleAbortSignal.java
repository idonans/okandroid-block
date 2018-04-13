package com.okandroid.block.lang;

public class SimpleAbortSignal implements AbortSignal {

    private boolean mAbort;

    public void abort() {
        if (!mAbort) {
            mAbort = true;
        }
    }

    @Override
    public boolean isAbort() {
        return mAbort;
    }

}
