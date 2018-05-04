package com.okandroid.block.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.okandroid.block.AppInit;
import com.okandroid.block.core.service.CoreService;
import com.okandroid.block.lang.Singleton;
import com.okandroid.block.util.ContextUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import timber.log.Timber;

/**
 * 支持跨进程
 */
public class CoreServiceManager {

    private final Lock mLock = new ReentrantLock();
    private final Condition mRemoteCondition = mLock.newCondition();

    private ICoreServicesManager mICoreServicesManager;

    private static final Singleton<CoreServiceManager> sInstance =
            new Singleton<CoreServiceManager>() {
                @Override
                protected CoreServiceManager create() {
                    return new CoreServiceManager();
                }
            };

    public static CoreServiceManager getInstance() {
        return sInstance.get();
    }

    private CoreServiceManager() {
        Timber.v("init");

        connect();
    }

    public void connect() {
        ContextUtil.getContext().bindService(
                new Intent(ContextUtil.getContext(), CoreService.class),
                mConn,
                Context.BIND_AUTO_CREATE | Context.BIND_ABOVE_CLIENT);
    }


    public void disconnect() {
        ContextUtil.getContext().unbindService(mConn);
    }

    public boolean isConnect() {
        return mICoreServicesManager != null;
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

    @NonNull
    public ICoreServicesManager fetchRemote() throws RemoteException {
        try {
            mLock.tryLock(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            if (mICoreServicesManager == null) {
                mRemoteCondition.await(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }

        if (mICoreServicesManager == null) {
            throw new RemoteException("remote not found");
        }

        return mICoreServicesManager;
    }

    private void setRemote(IBinder service) {
        try {
            mLock.tryLock(AppInit.getRemoteTimeoutMs(), TimeUnit.MILLISECONDS);
            Timber.v("setRemote %s", service);
            if (service == null) {
                mICoreServicesManager = null;
            } else {
                mICoreServicesManager = ICoreServicesManager.Stub.asInterface(service);
            }
            mRemoteCondition.signalAll();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

}
