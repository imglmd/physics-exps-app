package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetLastRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(
        limit: Int
    ): Flow<List<ExperimentRun>> = repository.getLastRuns(limit)
}