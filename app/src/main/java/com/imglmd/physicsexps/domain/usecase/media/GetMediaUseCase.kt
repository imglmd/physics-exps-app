package com.imglmd.physicsexps.domain.usecase.media

import com.imglmd.physicsexps.domain.model.MediaList
import com.imglmd.physicsexps.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class GetMediaUseCase(
    private val mediaRepository: MediaRepository,
    private val getMediaSignedUrlUseCase: GetMediaSignedUrlUseCase
) {

    suspend operator fun invoke(
        runId: String
    ): Result<MediaList> = withContext(Dispatchers.IO) {

        runCatching {
            val mediaList = mediaRepository.getMedias(runId)

            val mediaWithSignedUrls = coroutineScope {
                mediaList.media.map { media ->
                    async {
                        val signedUrl = getMediaSignedUrlUseCase(media.url)

                        media.copy(url = signedUrl)
                    }
                }.awaitAll()
            }

            mediaList.copy(
                media = mediaWithSignedUrls
            )
        }
    }
}