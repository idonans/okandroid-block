package com.okandroid.block.lang;

import com.okandroid.block.util.AvailableUtil;
import java.lang.ref.WeakReference;

public class WeakAvailable implements Available {

    private WeakReference<Object> mWeakReference = new WeakReference<>(null);

    public WeakAvailable(Object object) {
        mWeakReference = new WeakReference<>(object);
    }

    @Override
    public boolean isAvailable() {
        return AvailableUtil.isAvailable(mWeakReference.get());
    }

    public void setObject(Object object) {
        mWeakReference = new WeakReference<>(object);
    }

    public Object getObject() {
        Object object = mWeakReference.get();
        if (AvailableUtil.isAvailable(object)) {
            return object;
        }
        return null;
    }
}
