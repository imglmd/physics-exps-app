package com.imglmd.physicsexps.domain.usecase.experiment

import com.imglmd.physicsexps.domain.repository.ExperimentMediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetExperimentImagesUseCase(
    private val experimentMediaRepository: ExperimentMediaRepository
) {
    suspend operator fun invoke(experimentId: String): Result<List<String>> =
        withContext(Dispatchers.IO) {
            runCatching {
                experimentMediaRepository.getImageUrls(experimentId)
            }
        }
}
