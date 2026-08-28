package com.imglmd.feature.experiment.domain.usecase.experiment

import com.imglmd.feature.experiment.domain.repository.ExperimentMediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetExperimentPreviewsUseCase(
    private val experimentMediaRepository: ExperimentMediaRepository
) {
    suspend operator fun invoke(): Result<Map<String, String>> =
        withContext(Dispatchers.IO) {
            runCatching {
                experimentMediaRepository.getPreviewUrls()
            }
        }
}
