package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.pow

class CoulombsLawExperiment: Experiment {
    override val id = "pendulum"
    override val name = "Закон Кулона"
    override val category = "Электричество"
    override val description = "Закон Кулона — это физический закон, описывающий силу, с которой электрически заряженные тела притягиваются или отталкиваются. Чем больше заряды, тем сильнее взаимодействие."
    override val inputFields = listOf(
        InputField("q1", "Величина первого заряда", "q1", "Кл"),
        InputField("q2", "Величина второго заряда", "q2", "Кл"),
        InputField("distance", "Расстояние между зарядами", "r", "метр")
    )

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val q1 = inputs["q1"]!!
        val q2 = inputs["q2"]!!
        val r = inputs["distance"]!!
        val k = 9 * 1e9;
        val F = k * (Math.abs(q1 * q2)/r*r);

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Величина первого заряда", "q1", q1, "Кл"),
                PhysicalQuantity("Величина второго заряда", "q2", q2, "Кл"),
                PhysicalQuantity("Расстояние между зарядами", "r", r, "м"),
                PhysicalQuantity("Сила взаимодействия зарядов", "F", F, "Н"),
            ),
            "${LocalDate.now()}"
        )
    }
}