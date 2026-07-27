package com.imglmd.feature.history.domain.usecase

import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository

class DeleteAllRunsUseCase(
    val runsRepository: ExperimentRunsRepository
) {
    suspend operator fun invoke(){
        runsRepository.deleteAll()
    }
}