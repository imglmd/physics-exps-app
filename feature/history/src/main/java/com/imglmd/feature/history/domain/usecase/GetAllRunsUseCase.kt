package com.imglmd.feature.history.domain.usecase

import com.imglmd.feature.history.domain.model.ExperimentRun
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetAllRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(): Flow<List<ExperimentRun>> = repository.getAllExps()
}