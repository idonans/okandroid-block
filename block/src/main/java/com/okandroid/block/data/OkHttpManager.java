package com.okandroid.block.data;

import android.text.TextUtils;

import com.okandroid.block.AppInit;
import com.okandroid.block.core.CookieStoreManager;
import com.okandroid.block.lang.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * okhttp3
 */
public class OkHttpManager {

    private static final Singleton<OkHttpManager> sInstance =
            new Singleton<OkHttpManager>() {
                @Override
                protected OkHttpManager create() {
                    return new OkHttpManager();
                }
            };

    private static boolean sInit;

    public static OkHttpManager getInstance() {
        OkHttpManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private final OkHttpClient mOkHttpClient;

    private OkHttpManager() {
        Timber.v("init");
        Interceptor defaultUserAgentInterceptor =
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (chain.request().header("User-Agent") != null) {
                            return chain.proceed(chain.request());
                        }

                        String defaultUserAgent = AppInit.getDefaultUserAgent();
                        if (TextUtils.isEmpty(defaultUserAgent)) {
                            return chain.proceed(chain.request());
                        }

                        return chain.proceed(
                                chain.request()
                                        .newBuilder()
                                        .header("User-Agent", defaultUserAgent)
                                        .build());
                    }
                };

        boolean debug = AppInit.isDebug();
        if (debug) {
            Timber.d("in debug mode, config OkHttpClient.");

            Interceptor contentEncodingInterceptor =
                    new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Timber.d("contentEncodingInterceptor intercept");
                            Request request =
                                    chain.request()
                                            .newBuilder()
                                            .header("Accept-Encoding", "identity")
                                            .build();
                            return chain.proceed(request);
                        }
                    };

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Timber.d(message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            mOkHttpClient =
                    new OkHttpClient.Builder()
                            .addInterceptor(defaultUserAgentInterceptor)
                            .addInterceptor(contentEncodingInterceptor)
                            .addInterceptor(httpLoggingInterceptor)
                            .cookieJar(new OkHttp3CookieJar())
                            .build();
        } else {
            mOkHttpClient =
                    new OkHttpClient.Builder()
                            .addInterceptor(defaultUserAgentInterceptor)
                            .cookieJar(new OkHttp3CookieJar())
                            .build();
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private static class OkHttp3CookieJar implements CookieJar {

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url == null) {
                return;
            }

            if (cookies != null && !cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    if (cookie != null) {
                        CookieStoreManager.getInstance().save(url.toString(), Arrays.asList(cookie.toString()));
                    }
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookieList = new ArrayList<>();

            if (url == null) {
                return cookieList;
            }

            List<String> cookies = CookieStoreManager.getInstance().matches(url.toString());
            if (cookies != null && !cookies.isEmpty()) {
                for (String cookieString : cookies) {
                    Cookie cookie = Cookie.parse(url, cookieString);
                    if (cookie != null) {
                        cookieList.add(cookie);
                    }
                }
            }

            return cookieList;
        }
    }

}
