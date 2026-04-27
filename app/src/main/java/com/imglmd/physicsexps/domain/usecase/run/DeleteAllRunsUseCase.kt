package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository

class DeleteAllRunsUseCase(
    val runsRepository: ExperimentRunsRepository
) {
    suspend operator fun invoke(){
        runsRepository.deleteAll()
    }
}