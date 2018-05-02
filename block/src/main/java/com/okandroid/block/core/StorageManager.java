package com.okandroid.block.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.okandroid.block.AppInit;
import com.okandroid.block.core.service.StorageManagerService;
import com.okandroid.block.lang.Singleton;
import com.okandroid.block.util.ContextUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import timber.log.Timber;

public class StorageManager {

    public static final String NAMESPACE_SETTING = "block_core_storage_setting";
    public static final String NAMESPACE_CACHE = "block_core_storage_cache";

    private final Lock mLock = new ReentrantLock();
    private final Condition mRemoteCondition = mLock.newCondition();

    private IStorageManager mIStorageManager;

    private static final Singleton<StorageManager> sInstance =
            new Singleton<StorageManager>() {
                @Override
                protected StorageManager create() {
                    return new StorageManager();
                }
            };

    public static StorageManager getInstance() {
        return sInstance.get();
    }

    private StorageManager() {
        Timber.v("init");

        connect();
    }

    public void connect() {
        ContextUtil.getContext().bindService(
                new Intent(ContextUtil.getContext(), StorageManagerService.class),
                mConn,
                Context.BIND_AUTO_CREATE | Context.BIND_ABOVE_CLIENT);
    }


    public void disconnect() {
        ContextUtil.getContext().unbindService(mConn);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            setRemote(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setRemote(null);
        }
    };

    public void set(String namespace, String key, String value) {
        try {
            if (mIStorageManager == null) {
                mLock.tryLock(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            }

            if (mIStorageManager != null) {
                mIStorageManager.set(namespace, key, value);
            } else {
                throw new IllegalStateException("remote not found");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String get(String namespace, String key) {
        try {
            retainRemote();
            if (mIStorageManager == null) {
                throw new IllegalStateException("remote not found");
            }
            return mIStorageManager.get(namespace, key);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrSetLock(String namespace, String key, String setValue) {
        try {
            retainRemote();
            if (mIStorageManager == null) {
                throw new IllegalStateException("remote not found");
            }
            return mIStorageManager.getOrSetLock(namespace, key, setValue);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printAllRows(String namespace) {
        try {
            retainRemote();
            if (mIStorageManager == null) {
                throw new IllegalStateException("remote not found");
            }
            mIStorageManager.printAllRows(namespace);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void retainRemote() {
        try {
            mLock.tryLock(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            if (mIStorageManager == null) {
                mRemoteCondition.await(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    private void setRemote(IBinder service) {
        try {
            mLock.tryLock(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            Timber.v("setRemote %s", service);
            if (service == null) {
                mIStorageManager = null;
            } else {
                mIStorageManager = IStorageManager.Stub.asInterface(service);
            }
            mRemoteCondition.signalAll();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

}
