package com.imglmd.physicsexps.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDto(
    @SerialName("media_id")
    @Serializable(with = StringLikeSerializer::class)
    val mediaId: String,
    val filename: String,
    val url: String,
    val size: Int = 0,
    @SerialName("created_at")
    @Serializable(with = EpochMillisSerializer::class)
    val createdAt: Long = 0L,
    @SerialName("experiment_id")
    val experimentId: Int? = null
)

@Serializable
data class MediaListDto(
    @SerialName("run_id")
    @Serializable(with = StringLikeSerializer::class)
    val runId: String,
    val media: List<MediaDto>
)
