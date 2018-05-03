package com.okandroid.block.core;

import android.os.IBinder;
import android.os.RemoteException;

import com.okandroid.block.core.service.CoreService;
import com.okandroid.block.lang.Singleton;

import timber.log.Timber;

public class StorageManager {

    public static final String NAMESPACE_SETTING = "block_core_storage_setting";
    public static final String NAMESPACE_CACHE = "block_core_storage_cache";

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
    }

    public void set(String namespace, String key, String value) {
        try {
            IStorageManager storageManager = getStorageManagerService();
            storageManager.set(namespace, key, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String get(String namespace, String key) {
        try {
            IStorageManager storageManager = getStorageManagerService();
            return storageManager.get(namespace, key);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrSetLock(String namespace, String key, String setValue) {
        try {
            IStorageManager storageManager = getStorageManagerService();
            return storageManager.getOrSetLock(namespace, key, setValue);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printAllRows(String namespace) {
        try {
            IStorageManager storageManager = getStorageManagerService();
            storageManager.printAllRows(namespace);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private IStorageManager getStorageManagerService() throws RemoteException {
        ICoreServicesManager coreServicesManager = CoreServiceManager.getInstance().fetchRemote();
        IBinder binder = coreServicesManager.getCoreService(CoreService.CORE_SERVICE_STORAGE);
        return IStorageManager.Stub.asInterface(binder);
    }

}
