package com.imglmd.physicsexps.data.remote

import java.util.Date

data class MediaDto(
    val id: String,
    val filename: String,
    val url: String,
    val size: Int,
    val created_at: Long
)