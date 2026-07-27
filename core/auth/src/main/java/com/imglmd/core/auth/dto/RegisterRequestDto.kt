package com.imglmd.core.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val device_id: String,
    val device_name: String
)