package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.experiments.ExampleExperiment
import com.imglmd.physicsexps.domain.model.Experiment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val experimentsModule = module {

    factory<Experiment>(named("pendulum")) { ExampleExperiment() }

    single<List<Experiment>> {
        listOf(
            get(named("pendulum")),
        )
    }

    single { ExperimentRegistry(get()) }
}