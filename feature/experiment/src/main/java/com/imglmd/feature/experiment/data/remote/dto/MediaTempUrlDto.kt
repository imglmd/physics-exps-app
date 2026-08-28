package com.imglmd.feature.experiment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaTempUrlDto(
    val url: String,

    @SerialName("expires_in")
    val expiresIn: Int
)