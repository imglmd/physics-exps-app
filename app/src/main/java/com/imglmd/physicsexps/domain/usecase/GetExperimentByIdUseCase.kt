package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.Experiment

class GetExperimentByIdUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke(id: String): Experiment{
        return registry.getById(id)
    }
}