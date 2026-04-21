package com.imglmd.physicsexps.domain.model

data class InputField(
    val key: String, //time
    val label: String, //время
    val symbol: String, //t
    val unit: String, //сек

    val required: Boolean = false, // true только если поле обязятельное
    val min: Double? = null,
    val max: Double? = null
)