package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.TokenStorage
import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.dto.RegisterRequestDto
import com.imglmd.physicsexps.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: ApiService,
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