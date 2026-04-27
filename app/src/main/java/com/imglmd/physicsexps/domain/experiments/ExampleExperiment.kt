package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
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
        InputField("length", "Длина нити", "L", "м", min = 0.0, required = true),
        InputField("period", "Период колебаний", "T", "с", min = 0.0, required = true),
        InputField("gravity", "Ускорение свободного падения", "g", "м/с²", min = 0.0),
        InputField("angle", "Угол отклонения", "α", "°", required = true,
            min = 0.0, max = 90.0)
    )

    override val minRequiredInputs = 3

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {

        val L = inputs["length"]
        val T = inputs["period"]
        val g = inputs["gravity"]
        val a = inputs["angle"]

        val length: Double
        val period: Double
        val gravity: Double
        val frequency: Double
        val angularFrequency: Double
        val amplitude: Double
        val maxSpeed: Double
        val maxHeight: Double
        val map = mutableMapOf<String, Double>()

        when {
            L != null && T != null && a != null-> {
                gravity = (4 * PI.pow(2) * L) / T.pow(2)
                length = L
                period = T
                frequency = 1 / period
                angularFrequency = sqrt(gravity/length)
                val rad = a * PI / 180.0
                amplitude = length * sin(rad)
                maxSpeed = sqrt(2*gravity*length*(1 - cos(rad)))
                maxHeight = length*(1 - cos(rad))

                map.put("length", length)
                map.put("gravity", gravity)
            }

            T != null && g != null && a != null-> {
                length = (g * T.pow(2)) / (4 * PI.pow(2))
                period = T
                gravity = g
                frequency = 1 / period
                angularFrequency = sqrt(gravity/length)
                val rad = a * PI / 180.0
                amplitude = length * sin(rad)
                maxSpeed = sqrt(2*gravity*length*(1 - cos(rad)))
                maxHeight = length*(1 - cos(rad))

                map.put("gravity", gravity)
                map.put("length", length)
            }

            L != null && g != null && a != null-> {
                period = 2 * PI * sqrt(L / g)
                length = L
                gravity = g
                frequency = 1 / period
                angularFrequency = sqrt(gravity/length)
                val rad = a * PI / 180.0
                amplitude = length * sin(rad)
                maxSpeed = sqrt(2*gravity*length*(1 - cos(rad)))
                maxHeight = length*(1 - cos(rad))

                map.put("length", length)
                map.put("gravity", gravity)
            }

            else -> {
                error("")
            }
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Длина нити", "L", length, "м"),
                PhysicalQuantity("Период", "T", period, "с"),
                PhysicalQuantity("Ускорение", "g", gravity, "м/с²"),
                PhysicalQuantity("Частота колебаний", "V", frequency, "Гц"),
                PhysicalQuantity("Циклическая частота", "w₀", angularFrequency, "рад/с"),
                PhysicalQuantity("Амплитуда", "A", amplitude, "м"),
                PhysicalQuantity("Максимальная скорость", "vₘₐₓ", maxSpeed, "м/с"),
                PhysicalQuantity("Максимальная высота подъёма", "hₘₐₓ", maxHeight, "м")
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