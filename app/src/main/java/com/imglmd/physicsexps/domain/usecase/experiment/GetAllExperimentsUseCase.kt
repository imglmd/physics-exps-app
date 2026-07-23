package com.imglmd.physicsexps.domain.usecase.experiment

import com.imglmd.physicsexps.experiments.ExperimentRegistry
import com.imglmd.physicsexps.experiments.model.Experiment

class GetAllExperimentsUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke() : List<Experiment>{
        return registry.getAll()
    }
}