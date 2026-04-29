package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.experiments.CoulombsLawExperiment
import com.imglmd.physicsexps.domain.experiments.DopplerEffectExperiment
import com.imglmd.physicsexps.domain.experiments.ExampleExperiment
import com.imglmd.physicsexps.domain.experiments.FreeFallExperiment
import com.imglmd.physicsexps.domain.experiments.JouleLenzExperiment
import com.imglmd.physicsexps.domain.experiments.ProjectileMotionExperiment
import com.imglmd.physicsexps.domain.experiments.RadioactiveDecay
import com.imglmd.physicsexps.domain.experiments.SpringPendulumExperiment
import com.imglmd.physicsexps.domain.model.Experiment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val experimentsModule = module {

    single<List<Experiment>> {
        listOf(
            ExampleExperiment(),
            CoulombsLawExperiment(),
            FreeFallExperiment(),
            ProjectileMotionExperiment(),
            RadioactiveDecay(),
            SpringPendulumExperiment(),
            DopplerEffectExperiment(),
            JouleLenzExperiment()
        )
    }

    single { ExperimentRegistry(get()) }
}