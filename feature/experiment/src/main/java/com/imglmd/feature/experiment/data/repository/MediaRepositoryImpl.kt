package com.imglmd.feature.experiment.data.repository

import com.imglmd.feature.experiment.data.mapper.toUi
import com.imglmd.feature.experiment.data.remote.ApiService
import com.imglmd.feature.experiment.domain.model.Media
import com.imglmd.feature.experiment.domain.model.MediaList
import com.imglmd.feature.experiment.domain.repository.MediaRepository
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class MediaRepositoryImpl(
    private val apiService: ApiService,
): MediaRepository {
    override suspend fun getMedias(runId: String): MediaList {
        return apiService.getMedia(runId).toUi()
    }

    override suspend fun uploadMedia(
        runId: String, body: MultipartBody.Part, fileName: String
    ): Media {
        val fileNamePart = fileName.toRequestBody("text/plain".toMediaType())
        return apiService.uploadMedia(body, runId, fileNamePart).toUi()
    }

    override suspend fun delete(runId: String, id: String) {
        apiService.deleteMedia(runId, id)
    }
}
