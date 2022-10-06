package org.evcs.android.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.base.networking.retrofit.serializer.BaseGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.EVCSApplication;
import org.evcs.android.network.serializer.DateTimeDeserializer;
import org.joda.time.DateTime;

import java.io.Serializable;

public final class StorageUtils {

    //Vars
    private static final SharedPreferences sp =
        EVCSApplication.getInstance().getSharedPreferences(
            BaseConfiguration.SHARED_PREFERENCES,
            Activity.MODE_PRIVATE);

    private StorageUtils() {}

    /**
     * A bunch of shared preferences utils methods to get and set different types of values
     */
    public static void storeInSharedPreferences(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public static void storeInSharedPreferences(String key, Integer value) {
        sp.edit().putInt(key, value).apply();
    }

    public static void storeInSharedPreferences(String key, Float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public static void storeInSharedPreferences(String key, Boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public static void storeInSharedPreferences(String key, Long value) {
        sp.edit().putLong(key, value).apply();
    }

    public static void storeInSharedPreferences(String key, Serializable value) {
        GsonBuilder builder = BaseGsonBuilder.getBaseGsonBuilder();
        builder.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());
        Gson gson = builder.create();
        String json = gson.toJson(value);
        storeInSharedPreferences(key, json);
    }

    public static String getStringFromSharedPreferences(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static Integer getIntFromSharedPreferences(String key, Integer defValue) {
        return sp.getInt(key, defValue);
    }

    public static Float getFloatFromSharedPreferences(String key, Float defValue) {
        return sp.getFloat(key, defValue);
    }

    public static Boolean getBooleanFromSharedPreferences(String key, Boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static Long getLongFromSharedPreferences(String key, Long defValue) {
        return sp.getLong(key, defValue);
    }

    public static void clearKey(String key) {
        sp.edit().remove(key).apply();
    }

    public static boolean keyExists(String key) {
        return sp.contains(key);
    }

}
