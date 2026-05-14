package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.experiments.*
import com.imglmd.physicsexps.domain.model.Experiment
import org.koin.dsl.module

val experimentsModule = module {

    single<List<Experiment>> {
        listOf(
            PendulumExperiment(),
            PhysicalPendulumExperiment(),
            SpringPendulumExperiment(),

            FreeFallExperiment(),
            ProjectileMotionExperiment(),
            HarmonicVibrationsExperiment(),

            CoulombsLawExperiment(),
            JouleLenzExperiment(),

            DopplerEffectExperiment(),
            RadioactiveDecayExperiment()
        )
    }

    single {
        ExperimentRegistry(get())
    }
}