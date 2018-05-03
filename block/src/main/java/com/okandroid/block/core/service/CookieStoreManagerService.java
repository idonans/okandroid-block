package com.okandroid.block.core.service;

import android.os.RemoteException;

import com.okandroid.block.core.ICookieStoreManager;

import java.util.List;

class CookieStoreManagerService extends ICookieStoreManager.Stub {

    @Override
    public void save(String url, List<String> setCookies) throws RemoteException {

    }

    @Override
    public List<String> matches(String url) throws RemoteException {
        return null;
    }

    @Override
    public List<String> get(String url) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getUrls() {
        return null;
    }

    @Override
    public void clear() throws RemoteException {

    }

    @Override
    public void clearSession() throws RemoteException {

    }

    @Override
    public void printAll() throws RemoteException {

    }

}
