package com.imglmd.physicsexps.domain.usecase.experiment

import com.imglmd.core.experiments.ExperimentRegistry
import com.imglmd.core.experiments.model.ExperimentResult
import com.imglmd.core.experiments.validation.ExperimentValidator
import com.imglmd.core.experiments.validation.ValidationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalculateExperimentUseCase( // todo хз пока куда деть, но куда-то надо
    private val registry: ExperimentRegistry,
    private val validator: ExperimentValidator
) {

    sealed class Result {
        data class Success(
            val result: ExperimentResult,
            val inputs: Map<String, Double>
        ): Result()
        data class ValidationError(val errors: List<com.imglmd.core.experiments.validation.ValidationError>): Result()
        data class Failure(val message: String): Result()
    }

    suspend operator fun invoke(
        experimentId: String,
        rawInputs: Map<String, String>
    ): Result = withContext(Dispatchers.Default){

        val experiment = registry.getById(experimentId)

        return@withContext when (val validation = validator.validate(experiment, rawInputs)) {

            is ValidationResult.Error -> {
                Result.ValidationError(validation.errors)
            }

            is ValidationResult.Success -> {
                try {
                    val result = experiment.calculate(validation.values)
                    Result.Success(
                        result = result,
                        inputs = validation.values
                    )
                } catch (e: Exception) {
                    Result.Failure(e.message ?: "Ошибка вычисления")
                }
            }
        }
    }
}