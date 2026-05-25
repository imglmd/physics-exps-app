package com.imglmd.physicsexps.domain.usecase.media

import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.RemoteConfig

class GetMediaSignedUrlUseCase(
    private val api: ApiService,
    private val remoteConfig: RemoteConfig
) {
    suspend operator fun invoke(privatePath: String): String {

        val response = api.getMediaTempUrl(privatePath)

        return remoteConfig.resolveUrl(response.url)
    }
}