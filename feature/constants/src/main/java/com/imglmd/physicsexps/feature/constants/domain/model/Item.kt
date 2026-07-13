package com.imglmd.physicsexps.feature.constants.domain.model

data class Item(
    val symbol: String,
    val unitRes: Int? = null,
    val value: NumberValue,
    val nameRes: Int
)

sealed interface NumberValue {
    data class Single(val value: Double): NumberValue
    data class Range(val from: Double, val to: Double): NumberValue
}