package com.jd.smartcloudmobilesdk.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * SharedPreferences工具类
 */
public class SpUtils {

    public static <T> void saveToLocal(Context context, String name, String key, T t) {
        if (context == null) {
            return;
        }

        SharedPreferences sp;
        if (name == null) {
            sp = getDefaultSharedPreferences(context);
        } else {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        if (sp == null) {
            return;
        }

        if (t instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) t).apply();
        } else if (t instanceof String) {
            sp.edit().putString(key, (String) t).apply();
        } else if (t instanceof Integer) {
            sp.edit().putInt(key, (Integer) t).apply();
        } else if (t instanceof Float) {
            sp.edit().putFloat(key, (Float) t).apply();
        } else if (t instanceof Long) {
            sp.edit().putLong(key, (Long) t).apply();
        }
    }

    public static <T> T getFromLocal(Context context, String name, String key, T defaultValue) {
        if (context == null) {
            return defaultValue;
        }

        SharedPreferences sp;
        if (name == null) {
            sp = getDefaultSharedPreferences(context);
        } else {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        if (sp == null) {
            return defaultValue;
        }

        Map<String, ?> map = sp.getAll();
        if (map == null) {
            return defaultValue;
        }

        if (map.get(key) == null) {
            return defaultValue;
        }

        return (T) map.get(key);
    }

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
