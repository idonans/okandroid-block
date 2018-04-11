package com.okandroid.block.data;

import android.app.Activity;
import android.os.Bundle;

import com.okandroid.block.AppEnvironment;
import com.okandroid.block.lang.Singleton;
import com.okandroid.block.lang.WeakAvailable;

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

    public Activity getTopActivity() {
        return mActivityLifecycleCallbacks.getTopActivity();
    }

    public boolean hasShownAnyActivity() {
        return getTopActivity() != null;
    }

    private final ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks =
            new ActivityLifecycleCallbacksImpl();

    private class ActivityLifecycleCallbacksImpl extends AppEnvironment.SimpleApplicationCallbacks {

        private WeakAvailable mCreatedActivityRef = new WeakAvailable(null);
        private WeakAvailable mStartedActivityRef = new WeakAvailable(null);
        private WeakAvailable mResumedActivityRef = new WeakAvailable(null);

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mCreatedActivityRef.setObject(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mStartedActivityRef.setObject(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mResumedActivityRef.setObject(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Activity resumedActivity = (Activity) mResumedActivityRef.getObject();
            if (resumedActivity == activity) {
                mResumedActivityRef.setObject(null);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Activity startedActivity = (Activity) mStartedActivityRef.getObject();
            if (startedActivity == activity) {
                mStartedActivityRef.setObject(null);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Activity createdActivity = (Activity) mCreatedActivityRef.getObject();
            if (createdActivity == activity) {
                mCreatedActivityRef.setObject(null);
            }
        }

        public Activity getTopActivity() {
            Activity activity = (Activity) mResumedActivityRef.getObject();
            if (activity != null) {
                return activity;
            }
            activity = (Activity) mStartedActivityRef.getObject();
            if (activity != null) {
                return activity;
            }
            activity = (Activity) mCreatedActivityRef.getObject();
            return activity;
        }
    }
}
