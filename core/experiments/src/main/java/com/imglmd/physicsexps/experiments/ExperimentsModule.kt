package com.imglmd.physicsexps.experiments

import com.imglmd.physicsexps.experiments.impl.*
import com.imglmd.physicsexps.experiments.impl.CoulombsLawExperiment
import com.imglmd.physicsexps.experiments.model.Experiment
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