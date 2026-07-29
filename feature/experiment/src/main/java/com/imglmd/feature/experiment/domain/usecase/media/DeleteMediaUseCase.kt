package com.imglmd.feature.experiment.domain.usecase.media

import com.imglmd.feature.experiment.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMediaUseCase(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(runId: String, id: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            mediaRepository.delete(runId, id)
        }
    }
}