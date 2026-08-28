package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.feature.experiment.domain.model.ExperimentRun
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow

class GetAllRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    operator fun invoke(): Flow<List<ExperimentRun>> = repository.getAllExps()
}