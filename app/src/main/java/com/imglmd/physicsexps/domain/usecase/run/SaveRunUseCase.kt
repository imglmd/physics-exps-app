package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository

class SaveRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
    private val resultsRepository: ResultsRepository
) {
    suspend operator fun invoke(result: ExperimentResult) {
        val resultId = resultsRepository.insert(result)
        runsRepository.insert(
            ExperimentRun(
                experimentId = result.experiment.id,
                date = result.date,
                inputData = result.quantities.joinToString { "${it.label}=${it.value}" },
                resultId = resultId
            )
        )
    }
}