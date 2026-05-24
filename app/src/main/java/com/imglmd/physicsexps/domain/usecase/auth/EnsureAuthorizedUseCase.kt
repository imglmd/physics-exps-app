package com.imglmd.physicsexps.domain.usecase.auth

import com.imglmd.physicsexps.domain.repository.AuthRepository

class EnsureAuthorizedUseCase(
    private val authRepository: AuthRepository,
    private val registerUseCase: RegisterUseCase,
    private val pingUseCase: PingUseCase
) {

    suspend operator fun invoke(
        deviceId: String,
        deviceName: String
    ): AuthState {
        if (authRepository.hasToken()) {
            return if (runCatching { pingUseCase() }.isSuccess) {
                AuthState.Authorized
            } else {
                AuthState.Offline
            }
        }

        val result = runCatching {
            registerUseCase(
                deviceId = deviceId,
                deviceName = deviceName
            )
        }

        return when {
            result.isSuccess -> AuthState.Authorized
            else -> AuthState.Offline
        }
    }
}

enum class AuthState {
    Idle,
    Authorizing,
    Authorized,
    Offline
}