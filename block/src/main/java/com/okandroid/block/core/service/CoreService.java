package com.okandroid.block.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.okandroid.block.AppInit;
import com.okandroid.block.core.ICoreServicesManager;

import java.util.HashMap;

public class CoreService extends Service {

    private static final HashMap<String, ServiceFetcher<IBinder>> CORE_SERVICE_FETCHERS = new HashMap<>();

    public static final String CORE_SERVICE_STORAGE = "core_service_storage";

    @Override
    public void onCreate() {
        super.onCreate();

        AppInit.init(this);
        CORE_SERVICE_FETCHERS.put(CORE_SERVICE_STORAGE, new StaticServiceFetcher<IBinder>() {
            @Override
            public IBinder createService() {
                return new StorageManagerService();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder.asBinder();
    }

    private ICoreServicesManager mBinder = new ICoreServicesManager.Stub() {
        @Override
        public IBinder getCoreService(String serviceName) throws RemoteException {
            ServiceFetcher<IBinder> fetcher = CORE_SERVICE_FETCHERS.get(serviceName);
            if (fetcher == null) {
                throw new IllegalArgumentException("core service not found " + serviceName);
            }
            IBinder service = fetcher.getService();
            if (service == null) {
                throw new IllegalStateException("fetch service fail " + serviceName + " " + fetcher);
            }
            return service;
        }
    };

    private interface ServiceFetcher<T> {
        T getService();
    }

    private static abstract class StaticServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        @Override
        public final T getService() {
            synchronized (StaticServiceFetcher.this) {
                if (mCachedInstance == null) {
                    mCachedInstance = createService();
                }
                return mCachedInstance;
            }
        }

        public abstract T createService();
    }

}