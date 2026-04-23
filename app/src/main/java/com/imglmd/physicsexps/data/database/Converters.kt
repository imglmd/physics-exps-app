package com.imglmd.physicsexps.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imglmd.physicsexps.domain.model.ExperimentResult

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun fromMap(resultExp: ExperimentResult): String {
        return gson.toJson(resultExp)
    }

    @TypeConverter
    fun mapToString(resultString: String ): ExperimentResult {
        val type = object : TypeToken<ExperimentResult>(){}.type
        return gson.fromJson(resultString, type)
    }
}