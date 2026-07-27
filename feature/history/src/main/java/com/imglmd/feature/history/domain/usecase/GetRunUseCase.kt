package com.imglmd.feature.history.domain.usecase

import com.imglmd.feature.history.domain.model.ExperimentRun
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository

class GetRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
) {
    suspend operator fun invoke(id: Int): ExperimentRun {
        return runsRepository.getExpById(id)
    }
}