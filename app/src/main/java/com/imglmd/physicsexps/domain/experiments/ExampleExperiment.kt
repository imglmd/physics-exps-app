package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class ExampleExperiment : Experiment {

    override val id = "pendulum"
    override val name = "Математический маятник"
    override val category = "Механика"
    override val description =
        "Математический маятник — это модель тела, подвешенного на невесомой нити, которое колеблется под действием силы тяжести."
    override val imageRes = R.drawable.pendulum

    override val xLabel =  "Длина нити, м"
    override val yLabel = "Период, с"
    override val inputFields = listOf(
        InputField("length", "Длина нити", "L", "м", min = 0.0),
        InputField("period", "Период колебаний", "T", "с", min = 0.0, required = true),
        InputField("gravity", "Ускорение свободного падения", "g", "м/с²", min = 0.0)
    )

    override val minRequiredInputs = 2

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {

        val L = inputs["length"]
        val T = inputs["period"]
        val g = inputs["gravity"]

        val length: Double
        val period: Double
        val gravity: Double
        val map = mutableMapOf<String, Double>()

        when {
            L != null && T != null -> {
                gravity = (4 * PI.pow(2) * L) / T.pow(2)
                length = L
                period = T
                map.put("length", length)
                map.put("gravity", gravity)
            }

            T != null && g != null -> {
                length = (g * T.pow(2)) / (4 * PI.pow(2))
                period = T
                gravity = g
                map.put("gravity", gravity)
                map.put("length", length)
            }

            L != null && g != null -> {
                period = 2 * PI * sqrt(L / g)
                length = L
                gravity = g
                map.put("length", length)
                map.put("gravity", gravity)
            }

            else -> {
                error("")
            }
        }

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Длина нити", "L", length, "м"),
                PhysicalQuantity("Период", "T", period, "с"),
                PhysicalQuantity("Ускорение", "g", gravity, "м/с²")
            ),
            points = getPoints(map),  //TODO: пример отсутствия графика у эксперимента. заменить на getPoints(map)
            date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()

        val l: Double = inputs.getValue("length")
        val g: Double = inputs.getValue("gravity")

        val startX = 0.0
        val step = l / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while (x <= l + step) {
            val y = 2 * Math.PI * sqrt(x / g)
            list.add(Pair(x, y))
            x += step
        }

        return list
    }
}