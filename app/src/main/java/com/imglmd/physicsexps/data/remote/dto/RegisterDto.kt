package com.imglmd.physicsexps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val device_id: String,
    val device_name: String
)

@Serializable
data class RegisterResponseDto(
    val token: String,
    val token_type: String,
    val expires_days: Int

)