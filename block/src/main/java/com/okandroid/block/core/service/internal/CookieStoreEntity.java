package com.okandroid.block.core.service.internal;

import android.support.annotation.Keep;

import okhttp3.Cookie;

@Keep
public class CookieStoreEntity {

    public String url;
    public String setCookie;
    public String savedKey;

    public static CookieStoreEntity valueOf(String url, String setCookie, Cookie cookie) {
        CookieStoreEntity entity = new CookieStoreEntity();
        entity.url = url;
        entity.setCookie = setCookie;
        entity.savedKey = cookie.domain() + "|" + cookie.path() + "|" + cookie.name();
        return entity;
    }

}
