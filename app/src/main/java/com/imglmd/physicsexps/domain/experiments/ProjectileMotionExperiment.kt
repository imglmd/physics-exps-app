package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ProjectileMotionExperiment : Experiment {
    override val id = "projectile_motion"
    override val name = "Движение тела, брошенного под углом к горизонту"
    override val category = "Кинематика"
    override val description = """
        Движение тела, брошенного под углом к горизонту — это сложное движение,
        при котором горизонтальная составляющая скорости постоянна, а вертикальная
        изменяется под действием силы тяжести. Поддерживается бросок с высоты.
    """.trimIndent()
    override val imageRes = R.drawable.projectile_motion

    override val inputFields = listOf(
        InputField(
            key = "start_speed",
            label = "Начальная скорость",
            symbol = "v₀",
            unit = "м/с",
            required = true,
            min = 0.01,
            max = 1000.0
        ),
        InputField(
            key = "angle",
            label = "Угол броска",
            symbol = "α",
            unit = "°",
            required = true,
            min = 0.0,
            max = 90.0
        )
    )
    override val additionalInputFields = listOf(
        InputField(
            key = "initial_height",
            label = "Начальная высота",
            symbol = "h₀",
            unit = "м",
            required = false,
            min = 0.0,
            max = 10_000.0
        )
    )

    override val xLabel = "x, м"
    override val yLabel = "y, м"
    override val minRequiredInputs = 2

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val v0 = inputs.getValue("start_speed")
        val angleDeg = inputs.getValue("angle")
        val h0 = inputs["initial_height"] ?: 0.0

        if (angleDeg == 0.0 && h0 == 0.0) {
            throw IllegalArgumentException("Угол 0° и высота 0: движение невозможно")
        }

        val alpha = Math.toRadians(angleDeg)
        val g = ExpConstants.GRAVITY

        val vx = v0 * cos(alpha)
        val vy0 = v0 * sin(alpha)

        // время подъёма до максимальной высоты
        val tRise = vy0 / g

        // максимальная высота (от нуля)
        val hMax = h0 + vy0.pow(2) / (2 * g)

        // полное время полёта
        // g*t²/2 - vy0*t - h0 = 0
        val discriminant = vy0.pow(2) + 2 * g * h0
        val tFull = (vy0 + sqrt(discriminant)) / g

        // дальность броска
        val range = vx * tFull

        // скорость в момент удара о землю
        val vyImpact = vy0 - g * tFull
        val vImpact = sqrt(vx.pow(2) + vyImpact.pow(2))

        // угол удара
        val impactAngle = Math.toDegrees(atan(abs(vyImpact) / vx))

        val pointInputs = mapOf(
            "start_speed" to v0,
            "angle" to alpha,
            "initial_height" to h0,
            "time_full" to tFull
        )

        return ExperimentResult(
            experimentId = id,
            quantities = listOf(
                PhysicalQuantity("Дальность броска", "L", range, "м"),
                PhysicalQuantity("Максимальная высота", "H", hMax, "м"),
                PhysicalQuantity("Время полёта", "t", tFull, "с"),
                PhysicalQuantity("Время подъёма", "t↑", tRise, "с"),
                PhysicalQuantity("Горизонтальная скорость", "vₓ", vx, "м/с"),
                PhysicalQuantity("Начальная верт. скорость", "vy₀", vy0, "м/с"),
                PhysicalQuantity("Скорость при ударе", "v", vImpact, "м/с"),
                PhysicalQuantity("Угол при ударе", "β", impactAngle, "°"),
            ),
            points = getPoints(pointInputs),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val v0 = inputs.getValue("start_speed")
        val alpha = inputs.getValue("angle")
        val h0 = inputs.getValue("initial_height")
        val tFull = inputs.getValue("time_full")
        val g = ExpConstants.GRAVITY

        // нет графика если угол 90
        if (abs(alpha - PI / 2) < 1e-6) {
            return emptyList()
        }

        val vx = v0 * cos(alpha)
        val vy0 = v0 * sin(alpha)
        val range = vx*tFull

        val points = mutableListOf<Pair<Double, Double>>()
        val step = tFull / ExpConstants.DEFAULT_POINTS_COUNT

        var t = 0.0
        while (t <= tFull) {
            val x = vx * t
            val y = h0 + vy0 * t - g * t.pow(2) / 2
            if (y < 0) break
            points.add(x to y)
            t += step
        }
        points.add(range to 0.0)
        return points
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        val g = ExpConstants.GRAVITY

        steps += SolutionStep.Theory(
            title = "Идея решения",
            body = "Движение раскладывается на два независимых: " +
                    "по горизонтали — равномерное, по вертикали — равноускоренное с ускорением g."
        )

        steps += SolutionStep.Formula(
            description = "Разложим начальную скорость на оси",
            expression = "v_x = v_0 \\cos(\\alpha) \\\\ \\quad v_{y0} = v_0 \\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "Запишем уравнения движения",
            expression = "x(t) = v_x t \\\\y(t) = h_0 + v_{y0} t - \\frac{g t^2}{2}"
        )

        steps += SolutionStep.Formula(
            description = "В момент падения высота равна нулю",
            expression = "0 = h_0 + v_{y0} t - \\frac{g t^2}{2}"
        )

        steps += SolutionStep.Formula(
            description = "Решаем квадратное уравнение",
            expression = "t = \\frac{v_{y0} + \\sqrt{v_{y0}^2 + 2 g h_0}}{g}"
        )

        /**
         * если входных данных нет, возвращается только теория без чисел
         * сейчас в этом смысла особо нет, но так лучше разделять.
         * в будущем мб добавлю объяснение теории на экран экспериментов
         */
        if (inputs == null) return steps

        val v0 = inputs.getValue("start_speed")
        val angleDeg = inputs.getValue("angle")
        val h0 = inputs["initial_height"] ?: 0.0
        val alpha = Math.toRadians(angleDeg)

        // вычисляються величины которые будут подставляться в формулы
        val vx = v0 * cos(alpha)
        val vy0 = v0 * sin(alpha)
        val discriminant = vy0.pow(2) + 2 * g * h0
        val tFull = (vy0 + sqrt(discriminant)) / g
        val range = vx * tFull
        val hMax = h0 + vy0.pow(2) / (2 * g)

        // лямбда для форматирования
        val fmt = { d: Double -> "%.2f".format(d) }

        steps += SolutionStep.Substitution(
            description = "Найдём горизонтальную скорость",
            expression = "v_x = ${fmt(v0)} \\cdot \\cos(${fmt(angleDeg)}^\\circ)",
            result = "v_x = ${fmt(vx)} \\text{м/с}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём вертикальную скорость",
            expression = "v_{y0} = ${fmt(v0)} \\cdot \\sin(${fmt(angleDeg)}^\\circ)",
            result = "v_{y0} = ${fmt(vy0)} \\text{м/с}"
        )

        steps += SolutionStep.Substitution(
            description = "Вычислим время полёта",
            expression = "t = \\frac{${fmt(vy0)} + \\sqrt{${fmt(vy0)}^2 + 2 \\cdot ${g} \\cdot ${
                fmt(
                    h0
                )
            }}}{${g}}",
            result = "t = ${fmt(tFull)} \\text{с}"
        )

        steps += SolutionStep.Formula(
            description = "Дальность полёта",
            expression = "L = v_x t"
        )

        steps += SolutionStep.Substitution(
            description = "Подставим значения",
            expression = "L = ${fmt(vx)} \\cdot ${fmt(tFull)}",
            result = "L = ${fmt(range)} \\text{м}"
        )

        steps += SolutionStep.Formula(
            description = "Максимальная высота",
            expression = "H = h_0 + \\frac{v_{y0}^2}{2g}"
        )

        steps += SolutionStep.Substitution(
            description = "Подставим значения",
            expression = "H = ${fmt(h0)} + \\frac{${fmt(vy0)}^2}{2 \\cdot ${g}}",
            result = "H = ${fmt(hMax)} \\text{м}"
        )

        // результаты
        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("Дальность броска", "L", range, "м"),
                PhysicalQuantity("Максимальная высота", "H", hMax, "м"),
                PhysicalQuantity("Время полёта", "t", tFull, "с")
            )
        )

        return steps
    }
}