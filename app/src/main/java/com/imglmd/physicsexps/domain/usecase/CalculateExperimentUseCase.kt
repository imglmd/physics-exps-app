package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.ExperimentResult

class CalculateExperimentUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke(
        experimentId: String,
        inputs: Map<String, Double>
    ): Result<ExperimentResult> = runCatching {
        val experiment = registry.getById(experimentId)

        //TODO validation

        experiment.calculate(inputs)
    }
}