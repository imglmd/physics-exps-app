package com.imglmd.physicsexps.data.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import java.lang.reflect.Type

class Converters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Experiment::class.java, ExperimentAdapter())
        .create()

    @TypeConverter
    fun resultToString(result: ExperimentResult): String = gson.toJson(result)

    @TypeConverter
    fun stringToResult(value: String): ExperimentResult =
        gson.fromJson(value, ExperimentResult::class.java)
}

class ExperimentAdapter : JsonSerializer<Experiment>, JsonDeserializer<Experiment> {

    override fun serialize(
        src: Experiment,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonObject().apply {
            addProperty("type", src::class.java.name)
            add("data", context.serialize(src))
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Experiment {
        val obj = json.asJsonObject
        val type = Class.forName(obj.get("type").asString)
        return context.deserialize(obj.get("data"), type)
    }
}