package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetAllRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(): Flow<List<ExperimentRun>> = repository.getAllExps()
}