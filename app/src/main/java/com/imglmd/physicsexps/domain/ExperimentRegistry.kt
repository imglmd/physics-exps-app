package com.imglmd.physicsexps.domain

import com.imglmd.physicsexps.domain.model.Experiment

class ExperimentRegistry(
    private val experiments: List<Experiment>
) {
    fun getAll(): List<Experiment> = experiments

    fun getById(id: String): Experiment =
        experiments.first { it.id == id }
}