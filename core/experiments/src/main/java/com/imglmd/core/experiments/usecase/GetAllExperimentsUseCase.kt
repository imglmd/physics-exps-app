package com.imglmd.core.experiments.usecase

import com.imglmd.core.experiments.ExperimentRegistry
import com.imglmd.core.experiments.model.Experiment

class GetAllExperimentsUseCase(
    private val registry: ExperimentRegistry
) {
    operator fun invoke() : List<Experiment>{
        return registry.getAll()
    }
}