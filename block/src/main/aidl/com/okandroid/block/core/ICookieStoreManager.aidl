package com.okandroid.block.core;

interface ICookieStoreManager {

    void save(in String url, in List<String> setCookies);
    List<String> get(in String url);
    void clear();
    void clearSession();
    void printAll();

}
