package com.imglmd.feature.history.domain.usecase

import com.imglmd.feature.history.domain.model.ExperimentRun
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetLastRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(
        limit: Int
    ): Flow<List<ExperimentRun>> = repository.getLastRuns(limit)
}