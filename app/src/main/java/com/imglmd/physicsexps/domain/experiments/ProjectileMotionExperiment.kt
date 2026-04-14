package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.pow
import kotlin.math.sin

class ProjectileMotionExperiment : Experiment {
    override val id = "projectile_motion"
    override val name = "Движение тела, брошенного под углом к горизонту"
    override val category = "Кинематика"
    override val description = "Движение тела, брошенного под углом к горизонту — это движение, при котором тело запускают с начальной скоростью под углом к горизонтальной поверхности."
    override val inputFields = listOf(
        InputField("start_speed", "Начальная скорость", "v₀", "м/c"),
        InputField("start_height", "Начальная высота", "h₀", "м"),
        InputField("angle", "Угол броска", "a", "°")
    )

    override val minRequiredInputs = 3

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val v0 = inputs["start_speed"]
        val h0 = inputs["start_height"]
        val a = inputs["angle"]
        val g = 9.81
        val h_max: Double
        val L: Double
        val t_full: Double
        val t_rise: Double
        when {
            v0 != null && a != null && h0 != null -> {
                h_max = (v0.pow(2) * sin(a).pow(2)) / 2 * g + h0
                L = (v0.pow(2) * sin(2*a)) / g
                t_full = (2 * v0 * sin(a)) / g
                t_rise = (v0 * sin(a)) / g
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
            "${LocalDate.now()}"
        )
    }
}