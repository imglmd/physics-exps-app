package com.imglmd.physicsexps.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperimentPreviewDto(
    @Serializable(with = StringLikeSerializer::class)
    val id: String,
    val name: String,
    @SerialName("preview_image_url")
    val previewImageUrl: String
)

@Serializable
data class ExperimentImagesDto(
    @SerialName("experiment_id")
    @Serializable(with = StringLikeSerializer::class)
    val experimentId: String,
    @SerialName("image_urls")
    val imageUrls: List<String>
)
