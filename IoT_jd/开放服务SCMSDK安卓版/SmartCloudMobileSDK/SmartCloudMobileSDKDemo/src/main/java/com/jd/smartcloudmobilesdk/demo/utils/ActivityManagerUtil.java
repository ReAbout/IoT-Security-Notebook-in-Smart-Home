package com.jd.smartcloudmobilesdk.demo.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理工具类
 * Created by yangchangan on 2017/8/11.
 */
public class ActivityManagerUtil {
    private static Stack<Activity> mActivityStack;

    public static void pushActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }

        if (activity != null) {
            mActivityStack.push(activity);
        }
    }

    public static void popActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }

        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    public static <T extends Activity> Activity getActivity(Class<T> cls) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }

        for (Activity activity : mActivityStack) {
            if (activity != null && activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }
}
