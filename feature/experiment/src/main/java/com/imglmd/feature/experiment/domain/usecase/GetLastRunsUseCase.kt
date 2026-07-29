package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.feature.experiment.domain.model.ExperimentRun
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetLastRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(
        limit: Int
    ): Flow<List<ExperimentRun>> = repository.getLastRuns(limit)
}