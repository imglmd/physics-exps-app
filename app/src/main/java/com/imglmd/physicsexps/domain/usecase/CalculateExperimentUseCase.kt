package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.ExperimentResult

class CalculateExperimentUseCase(
    private val registry: ExperimentRegistry
) {

    sealed class Result {
        data class Success(val result: ExperimentResult): Result()
        data class ValidationError(val errors: List<com.imglmd.physicsexps.domain.validation.ValidationError>): Result()
        data class Failure(val message: String): Result()
    }

    operator fun invoke(
        experimentId: String,
        inputs: Map<String, Double>
    ): Result<ExperimentResult> = runCatching {
        val experiment = registry.getById(experimentId)

        experiment.calculate(inputs)
    }

}