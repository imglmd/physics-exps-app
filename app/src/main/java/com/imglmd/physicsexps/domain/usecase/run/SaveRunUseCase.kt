package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import kotlinx.serialization.json.Json

class SaveRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
    private val resultsRepository: ResultsRepository
) {
    private val json = Json

    suspend operator fun invoke(
        result: ExperimentResult,
        inputs: Map<String, Double>
    ): Int {
        val runId = runsRepository.insert(
            ExperimentRun(
                experimentId = result.experimentId,
                date = result.date,
                inputData = json.encodeToString(inputs),
            )
        )
        resultsRepository.insert(runId, result)
        return runId
    }
}