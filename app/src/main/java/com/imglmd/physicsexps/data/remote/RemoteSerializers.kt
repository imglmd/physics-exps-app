package com.imglmd.physicsexps.data.remote

import java.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

object StringLikeSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringLike", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: return decoder.decodeString()
        val primitive = jsonDecoder.decodeJsonElement().jsonPrimitive
        return primitive.contentOrNull ?: primitive.toString()
    }
}

object EpochMillisSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("EpochMillis", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeLong(value)
    }

    override fun deserialize(decoder: Decoder): Long {
        val jsonDecoder = decoder as? JsonDecoder ?: return decoder.decodeLong()
        return parseEpochMillis(jsonDecoder.decodeJsonElement().jsonPrimitive)
    }

    private fun parseEpochMillis(primitive: JsonPrimitive): Long {
        primitive.longOrNull?.let { return it }

        val content = primitive.contentOrNull?.trim().orEmpty()
        if (content.isEmpty()) {
            return 0L
        }

        return content.toLongOrNull()
            ?: runCatching { Instant.parse(content).toEpochMilli() }.getOrDefault(0L)
    }
}
