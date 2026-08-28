package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository

class DeleteRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
) {
    suspend operator fun invoke(runId: Int) {
        val run = runsRepository.getExpById(runId)
        runsRepository.delete(run)
    }
}