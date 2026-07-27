package com.imglmd.core.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    val token: String,
    val token_type: String,
    val expires_days: Int

)