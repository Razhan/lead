package com.ef.newlead.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreUtils {

    private static final String NAME = "save_new_lead";
    private static Context mContext;

    private SharedPreUtils() {
    }

    public static void init(Context context) {
        mContext = context;
    }

    public static void set(String key, Object data) {

        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();

        if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }

        editor.apply();
    }

    public static Object get(String key, Object defValue) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        if (defValue instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof String) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if (defValue instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }

    public static boolean contain(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return prefs.contains(key);
    }

}
