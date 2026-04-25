package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

class CoulombsLawExperiment: Experiment {
    override val id = "coulombs_law"
    override val name = "Закон Кулона"
    override val category = "Электричество"
    override val description = "Закон Кулона — это физический закон, описывающий силу, с которой электрически заряженные тела притягиваются или отталкиваются. Чем больше заряды, тем сильнее взаимодействие."
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
        val k = 9 * 1e9;
        val f: Double
        val map = mutableMapOf<String, Double>()

        when {
            q1 != null && q2 != null && r != null -> {
                f = k * (abs(q1 * q2*1e-18) /(r*r)) * 1e9
                map.put("q1", q1)
                map.put("q2", q2)
                map.put("distance", r)
            }

            else -> throw IllegalArgumentException("Нужно ввести любые три величины")
        }

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Первый заряд", "q1", q1, "нКл"),
                PhysicalQuantity("Второй заряд", "q2", q2, "нКл"),
                PhysicalQuantity("Расстояние", "r", r, "м"),
                PhysicalQuantity("Электрическая сила", "F", f, "нН")
            ),
            points = getPoints(map),
            date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
            xLabel = xLabel,
            yLabel = yLabel

        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val k = 9 * 1e9;
        val q1: Double = inputs.getValue("q1") * 1e-9
        val q2: Double = inputs.getValue("q2") * 1e-9
        val r: Double = inputs.getValue("distance")

        val startX = r * 0.1
        val step = (r - startX) / 100.0

        var x = startX
        while(x <= r + step) {
            val y = (k * (abs(q1 * q2) /(x*x))) * 1e9
            list.add(Pair(x, y))
            x += step
        }

        return list
    }
}