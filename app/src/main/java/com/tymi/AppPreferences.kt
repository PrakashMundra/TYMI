package com.tymi

import android.content.Context
import android.content.SharedPreferences


object AppPreferences {
    private val PREFS_NAME = "TYMI"
    private var instance: AppPreferences? = null
    private var prefs: SharedPreferences? = null

    val USER_PROFILE = "USER_PROFILE"
    val TRIGGER_TIME = "TRIGGER_TIME"

    fun getInstance(context: Context): AppPreferences {
        if (instance == null) {
            instance = AppPreferences
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        return instance as AppPreferences
    }

    fun putString(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String): String {
        return prefs?.getString(key, "")!!
    }

    fun putInt(key: String, value: Int) {
        prefs?.edit()?.putInt(key, value)?.apply()
    }

    fun getInt(key: String): Int {
        return prefs?.getInt(key, Constants.DEFAULT_POSITION)!!
    }

    fun putLong(key: String, value: Long) {
        prefs?.edit()?.putLong(key, value)?.apply()
    }

    fun getLong(key: String): Long {
        return prefs?.getLong(key, 0L)!!
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getBooleanDefaultFalse(key: String): Boolean {
        return prefs?.getBoolean(key, false)!!
    }

    fun getBooleanDefaultTrue(key: String): Boolean {
        return prefs?.getBoolean(key, true)!!
    }

    fun clearData() {
        prefs?.edit()?.clear()?.apply()
    }
}