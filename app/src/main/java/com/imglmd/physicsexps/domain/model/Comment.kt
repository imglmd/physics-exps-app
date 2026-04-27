package com.imglmd.physicsexps.domain.model

data class Comment(
    val id: Int = 0,
    val experimentRunId: Int,
    val text: String,
    val createdAt: Long = 0
)