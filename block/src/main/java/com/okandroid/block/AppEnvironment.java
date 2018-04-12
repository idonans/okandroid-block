package com.okandroid.block;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import com.okandroid.block.util.ContextUtil;

import java.util.HashMap;
import java.util.WeakHashMap;

import timber.log.Timber;

public class AppEnvironment {

    private static boolean sInit;

    private static final InternalApplicationCallbacks sInternalApplicationCallbacks =
            new InternalApplicationCallbacks();
    private static final AppProperties sAppProperties = new AppProperties();

    private AppEnvironment() {
    }

    private static void checkInitUnsafe() {
        if (!sInit) {
            throw new IllegalStateException("not init");
        }
    }

    static synchronized void init() {
        if (sInit) {
            return;
        }
        sInit = true;

        Timber.v("init");

        Application application = (Application) ContextUtil.getContext();
        application.registerActivityLifecycleCallbacks(sInternalApplicationCallbacks);
        application.registerComponentCallbacks(sInternalApplicationCallbacks);

        addApplicationCallbacks(sAppProperties);
    }

    public static AppProperties getAppProperties() {
        checkInitUnsafe();
        return sAppProperties;
    }

    public static void addApplicationCallbacks(ApplicationCallbacks callbacks) {
        checkInitUnsafe();
        sInternalApplicationCallbacks.addCallback(callbacks);
    }

    public static class AppProperties extends SimpleApplicationCallbacks {

        private final HashMap<String, Object> mProperties = new HashMap<>();

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            Timber.v("onConfigurationChanged clear properties");
            mProperties.clear();
        }

        public String getAppLabel() {
            final String key = "prop.appLabel";
            String appLabel = (String) mProperties.get(key);
            if (appLabel == null) {
                Context context = ContextUtil.getContext();
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo = context.getApplicationInfo();
                appLabel = String.valueOf(appInfo.loadLabel(pm));
                Timber.d("load property %s=%s", key, appLabel);
                mProperties.put(key, appLabel);
            }
            return appLabel;
        }

        public String getSubDirName() {
            final String key = "prop.subDirName";
            String subDirName = (String) mProperties.get(key);
            if (subDirName == null) {
                subDirName =
                        ContextUtil.getContext().getString(R.string.okandroid_block_sub_dir_name);
                if (TextUtils.isEmpty(subDirName)) {
                    subDirName = getAppLabel();
                }
                mProperties.put(key, subDirName);
            }
            return subDirName;
        }

        public boolean isFrescoEnable() {
            final String key = "prop.frescoEnable";
            Boolean frescoEnable = (Boolean) mProperties.get(key);
            if (frescoEnable == null) {
                frescoEnable =
                        ContextUtil.getContext()
                                .getResources()
                                .getBoolean(R.bool.okandroid_block_fresco_enable);
                Timber.v("load property %s=%s", key, String.valueOf(frescoEnable));
                mProperties.put(key, frescoEnable);
            }
            return frescoEnable;
        }

        public boolean isFresco565Config() {
            final String key = "prop.fresco565Config";
            Boolean fresco565Config = (Boolean) mProperties.get(key);
            if (fresco565Config == null) {
                fresco565Config =
                        ContextUtil.getContext()
                                .getResources()
                                .getBoolean(R.bool.okandroid_block_fresco_565_config);
                Timber.v("load property %s=%s", key, String.valueOf(fresco565Config));
                mProperties.put(key, fresco565Config);
            }
            return fresco565Config;
        }
    }

    public interface ApplicationCallbacks
            extends Application.ActivityLifecycleCallbacks,
            ComponentCallbacks,
            ComponentCallbacks2 {
    }

    private static class InternalApplicationCallbacks implements ApplicationCallbacks {

        private final Object mEmptyObject = new Object();
        private final WeakHashMap<ApplicationCallbacks, Object> mOuterCallbacks =
                new WeakHashMap<>();

        private void addCallback(ApplicationCallbacks callback) {
            synchronized (mOuterCallbacks) {
                mOuterCallbacks.put(callback, mEmptyObject);
            }
        }

        private Object[] getCallbacks() {
            Object[] callbacks = null;
            synchronized (mOuterCallbacks) {
                if (!mOuterCallbacks.isEmpty()) {
                    callbacks = mOuterCallbacks.keySet().toArray();
                }
            }
            return callbacks;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback)
                                .onActivityCreated(activity, savedInstanceState);
                    }
                }
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onActivityStarted(activity);
                    }
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onActivityResumed(activity);
                    }
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onActivityPaused(activity);
                    }
                }
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onActivityStopped(activity);
                    }
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback)
                                .onActivitySaveInstanceState(activity, outState);
                    }
                }
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onActivityDestroyed(activity);
                    }
                }
            }
        }

        @Override
        public void onTrimMemory(int level) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onTrimMemory(level);
                    }
                }
            }
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onConfigurationChanged(newConfig);
                    }
                }
            }
        }

        @Override
        public void onLowMemory() {
            Object[] callbacks = getCallbacks();
            if (callbacks != null) {
                for (Object callback : callbacks) {
                    if (callback instanceof ApplicationCallbacks) {
                        ((ApplicationCallbacks) callback).onLowMemory();
                    }
                }
            }
        }
    }

    public static class SimpleApplicationCallbacks implements ApplicationCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onTrimMemory(int level) {
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }

        @Override
        public void onLowMemory() {
        }
    }
}
