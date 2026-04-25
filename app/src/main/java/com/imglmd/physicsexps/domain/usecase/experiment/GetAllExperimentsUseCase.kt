package com.imglmd.physicsexps.domain.usecase.experiment

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.Experiment

class GetAllExperimentsUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke() : List<Experiment>{
        return registry.getAll()
    }
}