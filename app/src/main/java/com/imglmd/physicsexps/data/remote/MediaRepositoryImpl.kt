package com.imglmd.physicsexps.data.remote

import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val apiService: ApiService
): MediaRepository {
    override suspend fun getMedias(runId: Int): List<Media> {
        return apiService.getMedia(runId).map { it ->
            it.toUi()
        }
    }

    override suspend fun postMedia(): Media {
        return apiService.postMedia().toUi()
    }

}