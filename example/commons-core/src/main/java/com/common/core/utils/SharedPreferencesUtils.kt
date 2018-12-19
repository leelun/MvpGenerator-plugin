package com.common.core.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils private constructor(context: Context, name: String) {
    private val mSharedPreferences: SharedPreferences
    private val mEditor: SharedPreferences.Editor

    val all: Map<String, *>
        get() = mSharedPreferences.all

    init {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences.edit()
    }

    fun getString(s: String, s1: String): String? {
        return mSharedPreferences.getString(s, s1)
    }

    fun getStringSet(s: String, set: Set<String>): Set<String>? {
        return mSharedPreferences.getStringSet(s, set)
    }

    fun getInt(s: String, i: Int): Int {
        return mSharedPreferences.getInt(s, i)
    }

    fun getLong(s: String, l: Long): Long {
        return mSharedPreferences.getLong(s, l)
    }

    fun getFloat(s: String, v: Float): Float {
        return mSharedPreferences.getFloat(s, v)
    }

    fun getBoolean(s: String, b: Boolean): Boolean {
        return mSharedPreferences.getBoolean(s, b)
    }

    operator fun contains(s: String): Boolean {
        return mSharedPreferences.contains(s)
    }

    fun putString(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }

    fun putStringSet(key: String, values: Set<String>) {
        mEditor.putStringSet(key, values)
        mEditor.commit()
    }

    fun putInt(key: String, value: Int) {
        mEditor.putInt(key, value)
        mEditor.commit()
    }

    fun putLong(key: String, value: Long) {
        mEditor.putLong(key, value)
        mEditor.commit()
    }

    fun putFloat(key: String, value: Float) {
        mEditor.putFloat(key, value)
        mEditor.commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.commit()
    }

    fun remove(key: String) {
        mEditor.remove(key)
        mEditor.commit()
    }

    fun clear() {
        mEditor.clear()
        mEditor.commit()
    }

    companion object {
        private val NAME_SHAREPREFERENCES = "share_preferences"

        operator fun get(context: Context, name: String): SharedPreferencesUtils {
            return SharedPreferencesUtils(context, name)
        }

        operator fun get(context: Context): SharedPreferencesUtils {
            return SharedPreferencesUtils(context, NAME_SHAREPREFERENCES)
        }
    }
}