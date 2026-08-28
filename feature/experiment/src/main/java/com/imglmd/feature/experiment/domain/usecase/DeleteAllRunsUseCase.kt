package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository

class DeleteAllRunsUseCase(
    val runsRepository: ExperimentRunsRepository
) {
    suspend operator fun invoke(){
        runsRepository.deleteAll()
    }
}