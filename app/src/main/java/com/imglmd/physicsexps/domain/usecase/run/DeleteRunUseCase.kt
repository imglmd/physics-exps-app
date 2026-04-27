package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository

class DeleteRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
) {
    suspend operator fun invoke(runId: Int) {
        val run = runsRepository.getExpById(runId)
        runsRepository.delete(run)
    }
}