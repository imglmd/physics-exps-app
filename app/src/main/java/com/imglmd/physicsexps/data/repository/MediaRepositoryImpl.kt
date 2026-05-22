package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.RemoteConfig
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList
import com.imglmd.physicsexps.domain.repository.MediaRepository
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class MediaRepositoryImpl(
    private val apiService: ApiService,
    private val remoteConfig: RemoteConfig
) : MediaRepository {
    override suspend fun getMedias(runId: String): MediaList {
        return apiService.getMedia(runId).toUi(remoteConfig)
    }

    override suspend fun uploadMedia(
        runId: String, body: MultipartBody.Part, fileName: String
    ): Media {
        val fileNamePart = fileName.toRequestBody("text/plain".toMediaType())
        return apiService.uploadMedia(body, runId, fileNamePart).toUi(remoteConfig)
    }

    override suspend fun delete(runId: String, id: String) {
        apiService.deleteMedia(runId, id)
    }
}
