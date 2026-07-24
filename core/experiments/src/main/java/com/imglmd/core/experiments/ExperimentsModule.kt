package com.imglmd.core.experiments

import com.imglmd.core.experiments.impl.CoulombsLawExperiment
import com.imglmd.core.experiments.impl.DopplerEffectExperiment
import com.imglmd.core.experiments.impl.FreeFallExperiment
import com.imglmd.core.experiments.impl.HarmonicVibrationsExperiment
import com.imglmd.core.experiments.impl.JouleLenzExperiment
import com.imglmd.core.experiments.impl.PendulumExperiment
import com.imglmd.core.experiments.impl.PhysicalPendulumExperiment
import com.imglmd.core.experiments.impl.ProjectileMotionExperiment
import com.imglmd.core.experiments.impl.RadioactiveDecayExperiment
import com.imglmd.core.experiments.impl.SpringPendulumExperiment
import com.imglmd.core.experiments.model.Experiment
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