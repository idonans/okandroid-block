package com.okandroid.block.core;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.CheckResult;

import com.okandroid.block.core.service.CoreService;
import com.okandroid.block.lang.Singleton;

import java.util.List;

import timber.log.Timber;

/**
 * 支持跨进程
 */
public class CookieStoreManager {

    private static final Singleton<CookieStoreManager> sInstance =
            new Singleton<CookieStoreManager>() {
                @Override
                protected CookieStoreManager create() {
                    return new CookieStoreManager();
                }
            };

    public static CookieStoreManager getInstance() {
        return sInstance.get();
    }

    private CookieStoreManager() {
        Timber.v("init");
    }

    public void save(String url, List<String> setCookies) {
        try {
            ICookieStoreManager service = getService();
            service.save(url, setCookies);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @CheckResult
    public List<String> matches(String url) {
        try {
            ICookieStoreManager service = getService();
            return service.matches(url);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @CheckResult
    public List<String> get(String url) {
        try {
            ICookieStoreManager service = getService();
            return service.get(url);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @CheckResult
    public List<String> getUrls() {
        try {
            ICookieStoreManager service = getService();
            return service.getUrls();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        try {
            ICookieStoreManager service = getService();
            service.clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void clearSession() {
        try {
            ICookieStoreManager service = getService();
            service.clearSession();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void printAll() {
        try {
            ICookieStoreManager service = getService();
            service.printAll();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private ICookieStoreManager getService() throws RemoteException {
        ICoreServicesManager coreServices = CoreServiceManager.getInstance().fetchRemote();
        IBinder binder = coreServices.getCoreService(CoreService.CORE_SERVICE_COOKIE_STORE);
        return ICookieStoreManager.Stub.asInterface(binder);
    }

}
