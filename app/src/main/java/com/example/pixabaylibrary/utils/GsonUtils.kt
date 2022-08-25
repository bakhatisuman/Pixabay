package com.example.pixabaylibrary.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type
import java.util.HashMap

object GsonUtils {

    var TAG = GsonUtils::class.java.simpleName

    fun <T> toObject(data: String, type: Class<T>): T {
        var gson = Gson()
        return gson.fromJson(data, type)
    }

    /*   public static String toString(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }*/


    fun toString(src: Any?): String? {
        if (src == null) {
            return null
        }
        var builder = GsonBuilder()
        builder.setPrettyPrinting()
        var gson = builder.create()
        return gson.toJson(src)
    }


    fun <T> toObject(data: String?, type: Type): T? {
        try {
            var gson = Gson()
            return gson.fromJson<T>(data, type)
        } catch (ex: Exception) {
            Timber.v(ex.message)
            return null
        }

    }


    fun getJSONObject(src: Any): JSONObject? {
        var data = toString(src)
        Timber.v(data)
        try {
            return JSONObject(data)
        } catch (e: JSONException) {
            Timber.v(e.message)
        }

        return null
    }


    fun getJSONObject(data: String): JSONObject? {
        try {
            return JSONObject(data)
        } catch (e: JSONException) {
            Timber.v(e.message)
        }

        return null
    }


    fun getHashMap(src: Any): HashMap<String, String>? {
        var data = toString(src)
        Timber.v(data)
        return toObject<HashMap<String, String>>(data, object : TypeToken<HashMap<String, String>>() {

        }.type)
    }


}