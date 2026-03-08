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
        inputs.forEach{ (quantity, value) ->
            validate(quantity, value)
        }

        experiment.calculate(inputs)
    }

    private fun validate(quantity: String, value: Double): String {
        return when(quantity){
            "length" -> {
                if (value <= 0)
                    "Длина не может быть отрицательной и равной 0"
                else
                    ""
            }
            "period" -> {
                if (value <= 0)
                    "Период не может быть отрицательным и равным нулю"
                else
                    ""
            }

            else -> {
                ""
            }
        }

        //TODO() для всех величин доделать
    }
}