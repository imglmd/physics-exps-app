package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MediaRepository {
    suspend fun getMedias(runId: String): MediaList
    suspend fun uploadMedia(runId: String, body: MultipartBody.Part, fileName: String): Media

    suspend fun delete(runId: String, id: String)
}