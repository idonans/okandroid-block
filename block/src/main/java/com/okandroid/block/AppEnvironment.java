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

import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.util.ContextUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class AppEnvironment {

    private static final String TAG = "AppEnvironment";

    private static boolean sInit;

    private static final InternalApplicationCallbacks sInternalApplicationCallbacks =
            new InternalApplicationCallbacks();
    private static final AppProperties sAppProperties = new AppProperties();

    AppEnvironment() {}

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

        DelayLog.v(TAG, "init");

        Application application = (Application) ContextUtil.getContext();
        application.registerActivityLifecycleCallbacks(sInternalApplicationCallbacks);
        application.registerComponentCallbacks(sInternalApplicationCallbacks);

        addApplicationCallbacks(sAppProperties);
        syncLogVariable();
    }

    private static void syncLogVariable() {
        Log.setLogLevel(getAppProperties().getLogLevel());
        Log.setLogTag(getAppProperties().getLogTag());
        DelayLog.printAllDelayLog();
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

        private final String CLASS_NAME = ClassName.valueOf(this);
        private final HashMap<String, Object> mProperties = new HashMap<>();

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            DelayLog.v(CLASS_NAME, "onConfigurationChanged", "clear properties");
            mProperties.clear();

            // set Log variable on configuration changed
            syncLogVariable();
        }

        public String getAppLabel() {
            final String key = "prop.appLabel";
            String appLabel = (String) mProperties.get(key);
            if (appLabel == null) {
                Context context = ContextUtil.getContext();
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo = context.getApplicationInfo();
                appLabel = String.valueOf(appInfo.loadLabel(pm));
                DelayLog.v(CLASS_NAME, "load property", key, appLabel);
                mProperties.put(key, appLabel);
            }
            return appLabel;
        }

        public String getLogTag() {
            final String key = "prop.logTag";
            String logTag = (String) mProperties.get(key);
            if (logTag == null) {
                logTag = ContextUtil.getContext().getString(R.string.okandroid_block_log_tag);
                if (TextUtils.isEmpty(logTag)) {
                    logTag = getAppLabel();
                }
                DelayLog.v(CLASS_NAME, "load property", key, logTag);
                mProperties.put(key, logTag);
            }
            return logTag;
        }

        public int getLogLevel() {
            final String key = "prop.logLevel";
            Integer logLevelInt = (Integer) mProperties.get(key);
            if (logLevelInt == null) {
                String logLevel =
                        ContextUtil.getContext().getString(R.string.okandroid_block_log_level);
                if (TextUtils.isEmpty(logLevel)) {
                    logLevel = "WARN";
                }
                logLevel = logLevel.toUpperCase();

                final Map<String, Integer> level = new HashMap<>(5);
                level.put("VERBOSE", android.util.Log.VERBOSE);
                level.put("DEBUG", android.util.Log.DEBUG);
                level.put("INFO", android.util.Log.INFO);
                level.put("WARN", android.util.Log.WARN);
                level.put("ERROR", android.util.Log.ERROR);
                logLevelInt = level.get(logLevel);
                if (logLevelInt == null) {
                    throw new IllegalStateException(
                            "error log level["
                                    + logLevel
                                    + "], only VERBOSE DEBUG INFO WARN ERROR support");
                }
                DelayLog.v(CLASS_NAME, "load property", key, logLevelInt, logLevel);
                mProperties.put(key, logLevelInt);
            }
            return logLevelInt;
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
                DelayLog.v(CLASS_NAME, "load property", key, frescoEnable);
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
                DelayLog.v(CLASS_NAME, "load property", key, fresco565Config);
                mProperties.put(key, fresco565Config);
            }
            return fresco565Config;
        }
    }

    public interface ApplicationCallbacks
            extends Application.ActivityLifecycleCallbacks,
                    ComponentCallbacks,
                    ComponentCallbacks2 {}

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
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivityStopped(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

        @Override
        public void onActivityDestroyed(Activity activity) {}

        @Override
        public void onTrimMemory(int level) {}

        @Override
        public void onConfigurationChanged(Configuration newConfig) {}

        @Override
        public void onLowMemory() {}
    }
}
