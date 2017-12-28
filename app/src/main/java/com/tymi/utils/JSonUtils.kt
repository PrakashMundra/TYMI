package com.tymi.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.reflect.Type


object JSonUtils {
    private val TAG = JSonUtils::class.java.simpleName
    private var gson: Gson? = null

    init {
        val builder = GsonBuilder()
        gson = builder.create()
    }

    fun <T> fromJson(data: String, classOfT: Class<T>): T {
        Log.d(TAG, "decoding json: " + data)
        return gson!!.fromJson(data, classOfT)
    }

    private fun <T> fromJson(data: Reader, classOfT: Class<T>): T {
        Log.d(TAG, "decoding json from reader " + data)
        val t = gson!!.fromJson(data, classOfT)
        Log.d(TAG, "decoded object " + t)
        return t
    }

    fun <T> fromJson(data: InputStream, classOfT: Class<T>): T {
        Log.d(TAG, "decoding json from stream")
        return fromJson(InputStreamReader(data), classOfT)
    }

    fun <T> fromJson(data: String, collectionType: Type): Collection<T> {
        Log.d(TAG, "decoding json: " + data)
        return gson!!.fromJson(data, collectionType)
    }

    fun toJson(obj: Any?): String {
        var newObj = obj
        if (newObj == null) newObj = Any()
        return gson!!.toJson(newObj)
    }
}