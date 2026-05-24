package com.imglmd.physicsexps.domain.usecase.auth

import com.imglmd.physicsexps.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(deviceId: String, deviceName: String): Result<Unit> {
        return runCatching {
            authRepository.registerDevice(
                deviceId = deviceId,
                deviceName = deviceName
            )
        }
    }
}