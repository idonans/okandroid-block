package com.okandroid.block.core.service;

import android.os.RemoteException;

import com.okandroid.block.core.IStorageManager;

class StorageManagerService extends IStorageManager.Stub {
    @Override
    public void set(String namespace, String key, String value) throws RemoteException {
        StorageManagerProvider.getInstance().set(namespace, key, value);
    }

    @Override
    public String get(String namespace, String key) throws RemoteException {
        return StorageManagerProvider.getInstance().get(namespace, key);
    }

    @Override
    public String getOrSetLock(String namespace, String key, String setValue) throws RemoteException {
        return StorageManagerProvider.getInstance().getOrSetLock(namespace, key, setValue);
    }

    @Override
    public void printAllRows(String namespace) throws RemoteException {
        StorageManagerProvider.getInstance().printAllRows(namespace);
    }
}
