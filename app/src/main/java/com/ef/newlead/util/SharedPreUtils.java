package com.ef.newlead.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreUtils {

    private static final String NAME = "NewLeadSaved";
    private static Context mContext;
    private static SharedPreferences prefs;

    private SharedPreUtils() {
    }

    public static void init(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static int getInt(final String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static boolean getBoolean(final String key, final boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public static long getLong(final String key, final long defValue) {
        return prefs.getLong(key, defValue);
    }

    public static float getFloat(final String key, final float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public static String getString(final String key, final String defValue) {
        return prefs.getString(key, defValue);
    }

    public static void putInt(final String key, final int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public static void putBoolean(final String key, final Boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public static void putLong(final String key, final Long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public static void putFloat(final String key, final Float value) {
        prefs.edit().putFloat(key, value).apply();
    }

    public static void putString(final String key, final String value) {
        prefs.edit().putString(key, value).apply();
    }

    public static boolean contain(String key) {
        return prefs.contains(key);
    }

    public static void remove(final String key) {
        prefs.edit().remove(key).apply();
    }

}
