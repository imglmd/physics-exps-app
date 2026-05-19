package com.imglmd.physicsexps.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val id: String,
    val filename: String,
    val url: String,
    val size: Int,
    val created_at: Long
)