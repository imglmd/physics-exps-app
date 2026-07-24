package com.imglmd.physicsexps.experiments.usecase

import com.imglmd.physicsexps.experiments.ExperimentRegistry
import com.imglmd.physicsexps.experiments.model.Experiment

class GetAllExperimentsUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke() : List<Experiment>{
        return registry.getAll()
    }
}