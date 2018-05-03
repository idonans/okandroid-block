package com.okandroid.block.core.service;

import com.okandroid.block.core.ICookieStoreManager;

import java.util.List;

class CookieStoreManagerService extends ICookieStoreManager.Stub {

    @Override
    public void save(String url, List<String> setCookies) {
        CookieStoreManagerProvider.getInstance().save(url, setCookies);
    }

    @Override
    public List<String> matches(String url) {
        return CookieStoreManagerProvider.getInstance().matches(url);
    }

    @Override
    public List<String> get(String url) {
        return CookieStoreManagerProvider.getInstance().get(url);
    }

    @Override
    public List<String> getUrls() {
        return CookieStoreManagerProvider.getInstance().getUrls();
    }

    @Override
    public void clear() {
        CookieStoreManagerProvider.getInstance().clear();
    }

    @Override
    public void clearSession() {
        CookieStoreManagerProvider.getInstance().clearSession();
    }

    @Override
    public void printAll() {
        CookieStoreManagerProvider.getInstance().printAll();
    }

}
