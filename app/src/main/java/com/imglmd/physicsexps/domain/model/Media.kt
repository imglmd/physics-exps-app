package com.imglmd.physicsexps.domain.model

import kotlinx.serialization.Serializable

data class Media(
    val mediaId: String,
    val filename: String,
    val url: String,
    val size: Int,
    val createdAt: Long,
    val experimentId: Int?
)