package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import kotlin.math.abs
import kotlin.math.pow

class CoulombsLawExperiment: Experiment {
    override val id = "coulombs_law"
    override val name = "Закон Кулона"
    override val category = "Электричество"
    override val description = "Закон Кулона — это физический закон, описывающий силу, с которой электрически заряженные тела притягиваются или отталкиваются. Чем больше заряды, тем сильнее взаимодействие."
    override val imageRes = R.drawable.coulombs_law
    override val inputFields = listOf(
        InputField("q1", "Первый заряд × 10⁻⁹", "q1", "Кл"),
        InputField("q2", "Второй заряд × 10⁻⁹", "q2", "Кл"),
        InputField("distance", "Расстояние между зарядами", "r", "м")
    )
    override val xLabel = "Расстояние, м"
    override val yLabel = "Электрическая сила, нН"

    override val minRequiredInputs = 3

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val q1 = inputs["q1"]
        val q2 = inputs["q2"]
        val r = inputs["distance"]
        val k = ExpConstants.COULOMB_CONSTANT
        val nano = ExpConstants.NANO
        val nanoInv = ExpConstants.NANO_INVERSE

        val f: Double
        val intensity1: Double
        val intensity2: Double
        val potentialEnergy: Double
        val map = mutableMapOf<String, Double>()

        when {
            q1 != null && q2 != null && r != null -> {
                f = k * (abs(q1 * q2 * nano * nano) /(r*r)) * nanoInv
                intensity1 = k * q1 * ExpConstants.NANO / r.pow(2)
                intensity2 = k * q2 * ExpConstants.NANO / r.pow(2)
                potentialEnergy = k * (q1*q2*ExpConstants.NANO*ExpConstants.NANO/r) * ExpConstants.NANO_INVERSE
                map.put("q1", q1)
                map.put("q2", q2)
                map.put("distance", r)
            }

            else -> throw IllegalArgumentException("Нужно ввести любые три величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Первый заряд", "q1", q1, "нКл"),
                PhysicalQuantity("Второй заряд", "q2", q2, "нКл"),
                PhysicalQuantity("Расстояние", "r", r, "м"),
                PhysicalQuantity("Электрическая сила", "F", f, "нН"),
                PhysicalQuantity("Напряжённость 1-го заряда", "E1", intensity1, "В/м"),
                PhysicalQuantity("Напряжённость 2-го заряда", "E2", intensity2, "В/м"),
                PhysicalQuantity("Потенциальная энергия взаимодействия", "W",
                    potentialEnergy, "нДж")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val k = ExpConstants.COULOMB_CONSTANT
        val q1: Double = inputs.getValue("q1") * ExpConstants.NANO
        val q2: Double = inputs.getValue("q2") * ExpConstants.NANO
        val r: Double = inputs.getValue("distance")

        val startX = r * 0.1
        val step = (r - startX) / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while(x <= r + step) {
            val y = (k * (abs(q1 * q2) /(x*x))) * ExpConstants.NANO_INVERSE
            list.add(Pair(x, y))
            x += step
        }

        return list
    }
}