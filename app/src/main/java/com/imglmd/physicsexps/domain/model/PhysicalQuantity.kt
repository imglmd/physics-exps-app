package com.imglmd.physicsexps.domain.model

data class PhysicalQuantity(
    val label: String,
    val symbol: String,
    val value: Double,
    val unit: String,
)
