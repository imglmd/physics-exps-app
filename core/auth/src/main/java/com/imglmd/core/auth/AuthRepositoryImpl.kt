package com.imglmd.core.auth

import com.imglmd.core.auth.dto.RegisterRequestDto

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
): AuthRepository {
    override suspend fun registerDevice(
        deviceId: String,
        deviceName: String
    ) {
        val response = api.registerDevice(
            RegisterRequestDto(
                device_id = deviceId,
                device_name = deviceName
            )
        )
        tokenStorage.saveToken(response.token)
    }

    override fun getToken(): String? {
        return tokenStorage.getToken()
    }

    override fun hasToken(): Boolean {
        return !tokenStorage.getToken().isNullOrBlank()
    }
}