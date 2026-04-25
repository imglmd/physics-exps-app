package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository

class GetAllRunsUseCase(
    private val repository: ExperimentRunsRepository
) {
    suspend operator fun invoke(): List<ExperimentRun> {
        return repository.getAllExps()
    }
}