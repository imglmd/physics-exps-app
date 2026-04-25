package com.imglmd.physicsexps.domain.usecase.experiment

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.validation.ExperimentValidator
import com.imglmd.physicsexps.domain.validation.ValidationResult

class CalculateExperimentUseCase(
    private val registry: ExperimentRegistry,
    private val validator: ExperimentValidator
) {

    sealed class Result {
        data class Success(val result: ExperimentResult): Result()
        data class ValidationError(val errors: List<com.imglmd.physicsexps.domain.validation.ValidationError>): Result()
        data class Failure(val message: String): Result()
    }

    operator fun invoke(
        experimentId: String,
        rawInputs: Map<String, String>
    ): Result {

        val experiment = registry.getById(experimentId)

        return when (val validation = validator.validate(experiment, rawInputs)) {

            is ValidationResult.Error -> {
                Result.ValidationError(validation.errors)
            }

            is ValidationResult.Success -> {
                try {
                    val result = experiment.calculate(validation.values)
                    Result.Success(result.copy(inputs = validation.values))
                } catch (e: Exception) {
                    Result.Failure(e.message ?: "Ошибка вычисления")
                }
            }
        }
    }
}