package com.imglmd.physicsexps.domain.usecase.media

import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList
import com.imglmd.physicsexps.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMediaUseCase(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(runId: String): Result<MediaList> = withContext(Dispatchers.IO) {
        runCatching {
            mediaRepository.getMedias(runId)
        }

    }
}