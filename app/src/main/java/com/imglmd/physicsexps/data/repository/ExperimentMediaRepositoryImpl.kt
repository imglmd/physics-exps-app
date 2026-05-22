package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.RemoteConfig
import com.imglmd.physicsexps.domain.repository.ExperimentMediaRepository

class ExperimentMediaRepositoryImpl(
    private val apiService: ApiService,
    private val remoteConfig: RemoteConfig
) : ExperimentMediaRepository {
    override suspend fun getPreviewUrls(): Map<String, String> {
        return apiService.getExperiments().associate { dto ->
            dto.id to remoteConfig.resolveUrl(dto.previewImageUrl)
        }
    }

    override suspend fun getImageUrls(experimentId: String): List<String> {
        return apiService.getExperimentImages(experimentId).imageUrls
            .map(remoteConfig::resolveUrl)
    }
}
