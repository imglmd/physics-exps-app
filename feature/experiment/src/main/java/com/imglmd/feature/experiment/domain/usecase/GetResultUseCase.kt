package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.core.experiments.model.ExperimentResult
import com.imglmd.feature.experiment.domain.repository.ResultsRepository

class GetResultUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(runId: Int): ExperimentResult? =
        repository.getByRunId(runId)
}