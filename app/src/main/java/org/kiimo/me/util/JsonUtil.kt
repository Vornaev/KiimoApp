package org.kiimo.me.util

import com.google.gson.Gson

object JsonUtil{

    fun <T> getJsonStringFromModel(data: T): String {
        return Gson().toJson(data)
    }

    inline fun <reified T> loadModelFromJson(jsonString: String): T {
        return Gson().fromJson(jsonString, T::class.java)
    }

}