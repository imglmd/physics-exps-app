package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository

class DeleteRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(run: ExperimentRun) {
        runsRepository.delete(run)
        resultsRepository.deleteById(run.resultId)
    }
}