package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.experiments.CoulombsLawExperiment
import com.imglmd.physicsexps.domain.experiments.DopplerEffectExperiment
import com.imglmd.physicsexps.domain.experiments.PendulumExperiment
import com.imglmd.physicsexps.domain.experiments.FreeFallExperiment
import com.imglmd.physicsexps.domain.experiments.HarmonicVibrationsExperiment
import com.imglmd.physicsexps.domain.experiments.JouleLenzExperiment
import com.imglmd.physicsexps.domain.experiments.PhysicalPendulumExperiment
import com.imglmd.physicsexps.domain.experiments.ProjectileMotionExperiment
import com.imglmd.physicsexps.domain.experiments.RadioactiveDecayExperiment
import com.imglmd.physicsexps.domain.experiments.SpringPendulumExperiment
import com.imglmd.physicsexps.domain.model.Experiment
import org.koin.dsl.module

val experimentsModule = module {

    single<List<Experiment>> {
        listOf(
            PendulumExperiment(),
            CoulombsLawExperiment(),
            FreeFallExperiment(),
            ProjectileMotionExperiment(),
            RadioactiveDecayExperiment(),
            SpringPendulumExperiment(),
            DopplerEffectExperiment(),
            JouleLenzExperiment(),
            PhysicalPendulumExperiment(),
            HarmonicVibrationsExperiment()
        )
    }

    single { ExperimentRegistry(get()) }
}