package com.imglmd.physicsexps.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class MediaDto(
    @SerialName("media_id")
    val mediaId: String,
    val filename: String,
    val url: String,
    val size: Int = 0,
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @SerialName("experiment_id")
    val experimentId: Int? = null
)

@Serializable
data class MediaListDto(
    @SerialName("run_id")
    val runId: String,
    val media: List<MediaDto>
)