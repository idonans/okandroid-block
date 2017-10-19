package com.okandroid.block.data;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.lang.WeakAvailable;
import com.okandroid.block.util.ContextUtil;

/**
 * Activity 生命周期监听. 如可判断当前进程是否有正在显示的 Activity etc.
 */
public class ActivityLifecycleManager {

  private static class InstanceHolder {
    private static final ActivityLifecycleManager sInstance = new ActivityLifecycleManager();
  }

  private static boolean sInit;

  public static ActivityLifecycleManager getInstance() {
    ActivityLifecycleManager instance = InstanceHolder.sInstance;
    sInit = true;
    return instance;
  }

  public static boolean isInit() {
    return sInit;
  }

  private final String CLASS_NAME = ClassName.valueOf(this);

  private ActivityLifecycleManager() {
    Log.v(CLASS_NAME, "init");
    ((Application) ContextUtil.getContext()).registerActivityLifecycleCallbacks(
        mActivityLifecycleCallbacks);
  }

  public Activity getTopActivity() {
    return mActivityLifecycleCallbacks.getTopActivity();
  }

  public boolean hasShownAnyActivity() {
    return getTopActivity() != null;
  }

  private final ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks =
      new ActivityLifecycleCallbacksImpl();

  private class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    private WeakAvailable mCreatedActivityRef = new WeakAvailable(null);
    private WeakAvailable mStartedActivityRef = new WeakAvailable(null);
    private WeakAvailable mResumedActivityRef = new WeakAvailable(null);

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      mCreatedActivityRef.setObject(activity);
    }

    @Override public void onActivityStarted(Activity activity) {
      mStartedActivityRef.setObject(activity);
    }

    @Override public void onActivityResumed(Activity activity) {
      mResumedActivityRef.setObject(activity);
    }

    @Override public void onActivityPaused(Activity activity) {
      Activity resumedActivity = (Activity) mResumedActivityRef.getObject();
      if (resumedActivity == activity) {
        mResumedActivityRef.setObject(null);
      }
    }

    @Override public void onActivityStopped(Activity activity) {
      Activity startedActivity = (Activity) mStartedActivityRef.getObject();
      if (startedActivity == activity) {
        mStartedActivityRef.setObject(null);
      }
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override public void onActivityDestroyed(Activity activity) {
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
