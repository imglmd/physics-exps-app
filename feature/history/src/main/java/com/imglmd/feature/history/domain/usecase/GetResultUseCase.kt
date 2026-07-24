package com.imglmd.feature.history.domain.usecase

import com.imglmd.physicsexps.experiments.model.ExperimentResult
import com.imglmd.feature.history.domain.repository.ResultsRepository

class GetResultUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(runId: Int): ExperimentResult? =
        repository.getByRunId(runId)
}