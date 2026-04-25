package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.repository.ResultsRepository

class GetResultUseCase(
    private val repository: ResultsRepository
) {
    suspend operator fun invoke(id: Int): ExperimentResult? = repository.getById(id)
}