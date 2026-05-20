package com.imglmd.physicsexps.data.remote

import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList
import com.imglmd.physicsexps.domain.repository.MediaRepository
import okhttp3.MultipartBody

class MediaRepositoryImpl(
    private val apiService: ApiService
): MediaRepository {
    override suspend fun getMedias(runId: String): MediaList {
        return apiService.getMedia(runId).toUi()
    }

    override suspend fun uploadMedia(
        runId: String, body: MultipartBody.Part, fileName: String
    ): Media {
        return apiService.uploadMedia(body, runId, fileName).toUi()
    }

    override suspend fun delete(runId: String,id: String) {
        apiService.deleteMedia(runId, id)
    }

}