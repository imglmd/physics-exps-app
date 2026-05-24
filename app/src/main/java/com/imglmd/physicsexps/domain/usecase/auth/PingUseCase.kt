package com.imglmd.physicsexps.domain.usecase.auth

import com.imglmd.physicsexps.data.remote.ApiService

class PingUseCase(
    private val api: ApiService
) {
    suspend operator fun invoke() {
        api.health()
    }
}