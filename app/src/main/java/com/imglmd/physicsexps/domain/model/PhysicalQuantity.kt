package com.imglmd.physicsexps.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PhysicalQuantity(
    val label: String,
    val symbol: String,
    val value: Double,
    val unit: String,
)
