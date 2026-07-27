package com.imglmd.core.auth

import com.imglmd.core.auth.dto.RegisterRequestDto
import com.imglmd.core.auth.dto.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/register-device")
    suspend fun registerDevice(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto
}
