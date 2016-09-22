package com.ef.newlead.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public static void putStringSet(String name, String value) {
        Set<String> strings = prefs.getStringSet(name, new HashSet<>());
        strings.add(value);

        prefs.edit().putStringSet(name, strings).apply();
    }

    public static void removeStringSet(String name, String value) {
        Set<String> strings = prefs.getStringSet(name, new HashSet<>());

        if (strings.contains(value)) {
            strings.remove(value);
            prefs.edit().putStringSet(name, strings).apply();
        }
    }

    public static boolean containStringSet(String name, String value) {
        Set<String> strings = prefs.getStringSet(name, new HashSet<>());
        return strings.contains(value);
    }

    public static void putStringMap(String mapKeyName, String key, String value){
        Map<String,String> map = loadMap(mapKeyName);
        map.put(key, value);

        String jsonStr = new JSONObject(map).toString();
        prefs.edit().putString(mapKeyName, jsonStr).apply();
    }

    public static void removeStringMap(String mapKeyName, String key) {
        Map<String,String> map = loadMap(mapKeyName);

        if (map.containsKey(key)) {
            map.remove(key);
        }

        String jsonStr = new JSONObject(map).toString();
        prefs.edit().putString(mapKeyName, jsonStr).apply();
    }

    public static boolean containStringMap(String mapKeyName, String key) {
        Map<String,String> map = loadMap(mapKeyName);
        return map.containsKey(key);
    }

    public static Map<String,String> loadMap(String keyName){
        Map<String,String> outputMap = new HashMap<>();

        try {
            String jsonString = prefs.getString(keyName, (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();

            while(keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) jsonObject.get(key);
                outputMap.put(key, value);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return outputMap;
    }

}
