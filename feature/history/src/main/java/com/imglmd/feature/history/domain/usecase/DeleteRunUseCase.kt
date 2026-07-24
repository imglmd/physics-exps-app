package com.imglmd.feature.history.domain.usecase

import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository

class DeleteRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
) {
    suspend operator fun invoke(runId: Int) {
        val run = runsRepository.getExpById(runId)
        runsRepository.delete(run)
    }
}