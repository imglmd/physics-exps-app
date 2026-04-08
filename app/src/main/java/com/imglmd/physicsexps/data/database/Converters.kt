package com.imglmd.physicsexps.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun fromMap(resultMap: Map<String, Double>): String {
        return gson.toJson(resultMap)
    }

    @TypeConverter
    fun mapToString(resultString: String ): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>(){}.type
        return gson.fromJson(resultString, type)
    }
}