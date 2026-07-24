package com.imglmd.physicsexps.experiments.usecase

import com.imglmd.physicsexps.experiments.ExperimentRegistry
import com.imglmd.physicsexps.experiments.model.Experiment

class GetExperimentByIdUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke(id: String): Experiment {
        return registry.getById(id)
    }
}