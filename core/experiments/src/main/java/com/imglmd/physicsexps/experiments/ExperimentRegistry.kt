package com.imglmd.physicsexps.experiments

import com.imglmd.physicsexps.experiments.model.Experiment

class ExperimentRegistry(
    private val experiments: List<Experiment>
) {
    fun getAll(): List<Experiment> = experiments

    fun getById(id: String): Experiment =
        experiments.first { it.id == id }
}