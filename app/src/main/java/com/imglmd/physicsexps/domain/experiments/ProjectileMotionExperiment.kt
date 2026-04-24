package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin

class ProjectileMotionExperiment : Experiment {
    override val id = "projectile_motion"
    override val name = "Движение тела, брошенного под углом к горизонту"
    override val category = "Кинематика"
    override val description = "Движение тела, брошенного под углом к горизонту — это движение, при котором тело запускают с начальной скоростью под углом к горизонтальной поверхности."
    override val imageRes = R.drawable.projectile_motion
    override val inputFields = listOf(
        InputField("start_speed", "Начальная скорость", "v₀", "м/c"),
        InputField("angle", "Угол броска", "a", "°")
    )

    override val xLabel =  "x"
    override val yLabel = "y"

    override val minRequiredInputs = 2

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val v0 = inputs["start_speed"]
        val a_ = inputs["angle"]
        val a = Math.toRadians(a_!!)
        val g = ExpConstants.GRAVITY
        val h_max: Double
        val L: Double
        val t_full: Double
        val t_rise: Double
        val map = mutableMapOf<String, Double>()
        when {
            v0 != null && a != null -> {
                h_max = ((v0.pow(2) * sin(a).pow(2)) / (2 * g))
                L = (v0.pow(2) * sin(2*a)) / g
                t_full = (2 * v0 * sin(a)) / g
                t_rise = (v0 * sin(a)) / g
                map.put("angle", a)
                map.put("start_speed", v0)
                map.put("time_full", t_full)
            }

            else -> throw IllegalArgumentException("Нужно ввести три величины")
        }


        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Максимальная высота подъёма", "Hmax", h_max, "м"),
                PhysicalQuantity("Дальность броска", "L", L, "м"),
                PhysicalQuantity("Общее время движения", "t", t_full,"с"),
                PhysicalQuantity("Время подъёма", "t", t_rise, "с")
            ),
            points = getPoints(map),
            date = "${LocalDate.now()}",
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()

        val a = inputs.getValue("angle")
        val v0 = inputs.getValue("start_speed")
        val t_full = inputs.getValue("time_full")
        val g = ExpConstants.GRAVITY

        val startT = 0.0
        val step = t_full / ExpConstants.DEFAULT_POINTS_COUNT

        var t = startT
        while (t <= t_full + step) {
            val x = round( (v0 * cos(a) * t) * 10000.0) / 10000.0
            val y = round((v0 * sin(a) * t - (g*t.pow(2))/2) * 1000.0) / 1000.0
            list.add(Pair(x, y))
            t += step
        }
        return list
    }
}