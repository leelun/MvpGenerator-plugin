package com.common.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils {
    private static final String NAME_SHAREPREFERENCES = "share_preferences";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferencesUtils(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharedPreferencesUtils get(Context context, String name) {
        return new SharedPreferencesUtils(context, name);
    }

    public static SharedPreferencesUtils get(Context context) {
        return new SharedPreferencesUtils(context, NAME_SHAREPREFERENCES);
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    @Nullable
    public String getString(String s, String s1) {
        return mSharedPreferences.getString(s, s1);
    }

    @Nullable
    public Set<String> getStringSet(String s, Set<String> set) {
        return mSharedPreferences.getStringSet(s, set);
    }

    public int getInt(String s, int i) {
        return mSharedPreferences.getInt(s, i);
    }

    public long getLong(String s, long l) {
        return mSharedPreferences.getLong(s, l);
    }

    public float getFloat(String s, float v) {
        return mSharedPreferences.getFloat(s, v);
    }

    public boolean getBoolean(String s, boolean b) {
        return mSharedPreferences.getBoolean(s, b);
    }

    public boolean contains(String s) {
        return mSharedPreferences.contains(s);
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void putStringSet(String key, Set<String> values) {
        mEditor.putStringSet(key, values);
        mEditor.commit();
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}