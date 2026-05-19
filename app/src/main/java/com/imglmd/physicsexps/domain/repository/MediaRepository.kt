package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.Media

interface MediaRepository {
    suspend fun getMedias(runId: Int): List<Media>
    suspend fun postMedia(): Media
}