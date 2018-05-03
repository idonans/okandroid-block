package com.okandroid.block.core.service;

import android.os.RemoteException;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okandroid.block.core.service.internal.CookieStoreEntity;
import com.okandroid.block.db.SimpleDB;
import com.okandroid.block.lang.Singleton;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import timber.log.Timber;

class CookieStoreManagerProvider {

    private final SimpleDB mStore;

    private static final Singleton<CookieStoreManagerProvider> sInstance =
            new Singleton<CookieStoreManagerProvider>() {
                @Override
                protected CookieStoreManagerProvider create() {
                    return new CookieStoreManagerProvider();
                }
            };

    public static CookieStoreManagerProvider getInstance() {
        return sInstance.get();
    }

    private final Gson mGson = new Gson();
    private final Type mCookieStoreEntityType = new TypeToken<CookieStoreEntity>() {
    }.getType();

    private Map<String, Pair<CookieStoreEntity, Cookie>> mData = new HashMap<>();

    private CookieStoreManagerProvider() {
        Timber.v("init");
        mStore = new SimpleDB("block_core_cookie_store");
        mStore.trim(MAX_SIZE);

        Map<String, String> rows = mStore.getAll();
        if (rows != null) {
            for (Map.Entry<String, String> entry : rows.entrySet()) {
                try {
                    CookieStoreEntity entity = mGson.fromJson(entry.getValue(), mCookieStoreEntityType);
                    if (entity != null) {
                        mData.put(entry.getKey(), entity);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void save(String url, List<String> setCookies) throws RemoteException {
        if (TextUtils.isEmpty(url) || setCookies == null || setCookies.isEmpty()) {
            return;
        }

        for (String setCookie : setCookies) {
            if (TextUtils.isEmpty(setCookie)) {
                continue;
            }

            CookieStoreEntity entity = new CookieStoreEntity();
            entity.setCookie = setCookie;
            entity.url = url;
            entity.savedKey = buildSavedKey(setCookie);
        }
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

    /**
     * 存储的 cookie 条数的上限
     */
    private static final int MAX_SIZE = 5000;
}
