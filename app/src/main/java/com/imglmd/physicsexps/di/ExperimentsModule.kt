package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.experiments.ExampleExperiment
import com.imglmd.physicsexps.domain.model.Experiment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val experimentsModule = module {

    factory<Experiment> { ExampleExperiment() }

    single<List<Experiment>> {
        getAll()
    }

    single { ExperimentRegistry(get()) }
}