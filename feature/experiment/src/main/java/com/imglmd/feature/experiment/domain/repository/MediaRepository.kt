package com.imglmd.feature.experiment.domain.repository

import com.imglmd.feature.experiment.domain.model.Media
import com.imglmd.feature.experiment.domain.model.MediaList
import okhttp3.MultipartBody

interface MediaRepository {
    suspend fun getMedias(runId: String): MediaList
    suspend fun uploadMedia(runId: String, body: MultipartBody.Part, fileName: String): Media

    suspend fun delete(runId: String, id: String)
}