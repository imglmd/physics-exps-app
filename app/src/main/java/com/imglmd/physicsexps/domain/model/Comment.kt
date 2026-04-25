package com.imglmd.physicsexps.domain.model

data class Comment(
    val id: Int,
    val experimentRunId: Int,
    val text: String,
    val createdAt: Long
)