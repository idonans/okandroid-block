package com.okandroid.block.data;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.okandroid.block.AppEnvironment;
import com.okandroid.block.lang.Singleton;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * Activity 生命周期监听. 如可判断当前进程是否有正在显示的 Activity etc.
 */
public class ActivityLifecycleManager {

    private static final Singleton<ActivityLifecycleManager> sInstance =
            new Singleton<ActivityLifecycleManager>() {
                @Override
                protected ActivityLifecycleManager create() {
                    return new ActivityLifecycleManager();
                }
            };

    private static boolean sInit;

    public static ActivityLifecycleManager getInstance() {
        ActivityLifecycleManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private ActivityLifecycleManager() {
        Timber.v("init");
        AppEnvironment.addApplicationCallbacks(mActivityLifecycleCallbacks);
    }

    @Nullable
    public Activity getTopActivity() {
        return mActivityLifecycleCallbacks.getTopActivity();
    }

    public boolean hasShownAnyActivity() {
        return getTopActivity() != null;
    }

    private final ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks =
            new ActivityLifecycleCallbacksImpl();

    private class ActivityLifecycleCallbacksImpl extends AppEnvironment.SimpleApplicationCallbacks {

        @NonNull
        private WeakReference<Activity> mCreatedActivityRef = new WeakReference<>(null);
        @NonNull
        private WeakReference<Activity> mStartedActivityRef = new WeakReference<>(null);
        @NonNull
        private WeakReference<Activity> mResumedActivityRef = new WeakReference<>(null);

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mCreatedActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mStartedActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mResumedActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Activity resumedActivity = mResumedActivityRef.get();
            if (resumedActivity == activity) {
                mResumedActivityRef = new WeakReference<>(null);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Activity startedActivity = mStartedActivityRef.get();
            if (startedActivity == activity) {
                mStartedActivityRef = new WeakReference<>(null);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Activity createdActivity = mCreatedActivityRef.get();
            if (createdActivity == activity) {
                mCreatedActivityRef = new WeakReference<>(null);
            }
        }

        @Nullable
        public Activity getTopActivity() {
            Activity activity = mResumedActivityRef.get();
            if (activity != null) {
                return activity;
            }
            activity = mStartedActivityRef.get();
            if (activity != null) {
                return activity;
            }
            activity = mCreatedActivityRef.get();
            return activity;
        }
    }
}
