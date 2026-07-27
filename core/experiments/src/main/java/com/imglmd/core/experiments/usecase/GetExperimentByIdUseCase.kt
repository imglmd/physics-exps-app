package com.imglmd.core.experiments.usecase

import com.imglmd.core.experiments.ExperimentRegistry
import com.imglmd.core.experiments.model.Experiment

class GetExperimentByIdUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke(id: String): Experiment {
        return registry.getById(id)
    }
}