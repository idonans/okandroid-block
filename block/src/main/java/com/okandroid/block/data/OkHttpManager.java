package com.okandroid.block.data;

import android.text.TextUtils;
import com.okandroid.block.AppEnv;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * okhttp3
 */
public class OkHttpManager {

  private static class InstanceHolder {

    private static final OkHttpManager sInstance = new OkHttpManager();
  }

  private static boolean sInit;

  public static OkHttpManager getInstance() {
    OkHttpManager instance = InstanceHolder.sInstance;
    sInit = true;
    return instance;
  }

  public static boolean isInit() {
    return sInit;
  }

  private final String CLASS_NAME = ClassName.valueOf(this);
  private final OkHttpClient mOkHttpClient;

  private String mDefaultUserAgent;

  private OkHttpManager() {
    Log.v(CLASS_NAME, "init");
    Interceptor defaultUserAgentInterceptor = new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        if (chain.request().header("User-Agent") != null) {
          return chain.proceed(chain.request());
        }

        String defaultUserAgent = mDefaultUserAgent;
        if (TextUtils.isEmpty(defaultUserAgent)) {
          return chain.proceed(chain.request());
        }

        return chain.proceed(
            chain.request().newBuilder().header("User-Agent", defaultUserAgent).build());
      }
    };

    boolean debug = AppEnv.getLogLevelInt() <= android.util.Log.DEBUG;
    if (debug) {
      Log.d(CLASS_NAME, "in debug mode, config OkHttpClient.");

      Interceptor contentEncodingInterceptor = new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
          Log.d(CLASS_NAME + " contentEncodingInterceptor intercept");
          Request request =
              chain.request().newBuilder().header("Accept-Encoding", "identity").build();
          return chain.proceed(request);
        }
      };

      HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
      httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      mOkHttpClient = new OkHttpClient.Builder().addInterceptor(defaultUserAgentInterceptor)
          .addInterceptor(contentEncodingInterceptor)
          .addInterceptor(httpLoggingInterceptor)
          .cookieJar(CookiesManager.getInstance().getOkHttp3CookieJar())
          .build();
    } else {
      mOkHttpClient = new OkHttpClient.Builder().addInterceptor(defaultUserAgentInterceptor)
          .cookieJar(CookiesManager.getInstance().getOkHttp3CookieJar())
          .build();
    }
  }

  public OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public void setDefaultUserAgent(String defaultUserAgent) {
    mDefaultUserAgent = defaultUserAgent;
  }

  public String getDefaultUserAgent() {
    return mDefaultUserAgent;
  }
}
