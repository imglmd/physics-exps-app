package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.feature.experiment.domain.model.ExperimentRun
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository

class GetRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
) {
    suspend operator fun invoke(id: Int): ExperimentRun {
        return runsRepository.getExpById(id)
    }
}