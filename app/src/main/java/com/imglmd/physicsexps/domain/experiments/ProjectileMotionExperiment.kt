package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ProjectileMotionExperiment : Experiment {
    override val id = "projectile_motion"
    override val category = "kinematics"
    override val description = "projectile_motion_desc"

    override val inputFields = listOf(
        InputField(
            key = "start_speed",
            label = "initial_velocity",
            symbol = "v₀",
            unit = "m_s",
            required = true,
            min = 0.01,
            max = 1000.0
        ),
        InputField(
            key = "angle",
            label = "launch_angle",
            symbol = "α",
            unit = "ang",
            required = true,
            min = 0.0,
            max = 90.0
        )
    )
    override val additionalInputFields = listOf(
        InputField(
            key = "initial_height",
            label = "initial_height",
            symbol = "h₀",
            unit = "m",
            required = false,
            min = 0.0,
            max = 10_000.0
        )
    )

    override val xLabel = "x_m"
    override val yLabel = "y_m"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val speed = inputs["start_speed"]
        val angle = inputs["angle"]
        val height = inputs["initial_height"]

        val errors = mutableListOf<ValidationError>()

        if (speed == null) errors += ValidationError.RequiredField("start_speed")
        if (angle == null) errors += ValidationError.RequiredField("angle")

        if (speed != null && (speed <= 0.0 || speed > 1000.0)) {
            errors += ValidationError.OutOfRange(
                fieldKey = "start_speed",
                min = 0.01,
                max = 1000.0
            )
        }

        if (angle != null && (angle !in 0.0..90.0)) {
            errors += ValidationError.OutOfRange(
                fieldKey = "angle",
                min = 0.0,
                max = 90.0
            )
        }

        if (height != null && (height !in 0.0..10_000.0)) {
            errors += ValidationError.OutOfRange(
                fieldKey = "initial_height",
                min = 0.0,
                max = 10_000.0
            )
        }

        if (speed != null && angle != null && angle == 0.0 && (height ?: 0.0) == 0.0) {
            errors += ValidationError.InvalidCombination
        }

        return if (errors.isEmpty()) {
            ValidationResult.Success(inputs)
        } else {
            ValidationResult.Error(errors)
        }
    }

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val v0 = inputs.getValue("start_speed")
        val angleDeg = inputs.getValue("angle")
        val h0 = inputs["initial_height"] ?: 0.0

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
                PhysicalQuantity("th_r", "L", range, "m"),
                PhysicalQuantity("max_h", "H", hMax, "m"),
                PhysicalQuantity("f_time", "t", tFull, "s"),
                PhysicalQuantity("r_time", "t↑", tRise, "s"),
                PhysicalQuantity("h_vel", "vₓ", vx, "m_s"),
                PhysicalQuantity("in_ver_vel", "vy₀", vy0, "m_s"),
                PhysicalQuantity("impact_velocity", "v", vImpact, "m_s"),
                PhysicalQuantity("impact_angle", "β", impactAngle, "ang"),
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
            title = "solution_idea",
            body = "pr_step_1"
        )

        steps += SolutionStep.Formula(
            description = "pr_step_2",
            expression = "v_x = v_0 \\cos(\\alpha) \\\\ \\quad v_{y0} = v_0 \\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "pr_step_3",
            expression = "x(t) = v_x t \\\\y(t) = h_0 + v_{y0} t - \\frac{g t^2}{2}"
        )

        steps += SolutionStep.Formula(
            description = "pr_step_4",
            expression = "0 = h_0 + v_{y0} t - \\frac{g t^2}{2}"
        )

        steps += SolutionStep.Formula(
            description = "pr_step_5",
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
            description = "pr_step_6",
            expression = "v_x = ${fmt(v0)} \\cdot \\cos(${fmt(angleDeg)}^\\circ)",
            result = "v_x = ${fmt(vx)}"
        )

        steps += SolutionStep.Substitution(
            description = "pr_step_7",
            expression = "v_{y0} = ${fmt(v0)} \\cdot \\sin(${fmt(angleDeg)}^\\circ)",
            result = "v_{y0} = ${fmt(vy0)}"
        )

        steps += SolutionStep.Substitution(
            description = "pr_step_8",
            expression = "t = \\frac{${fmt(vy0)} + \\sqrt{${fmt(vy0)}^2 + 2 \\cdot ${g} \\cdot ${
                fmt(
                    h0
                )
            }}}{${g}}",
            result = "t = ${fmt(tFull)}"
        )

        steps += SolutionStep.Formula(
            description = "th_r",
            expression = "L = v_x t"
        )

        steps += SolutionStep.Substitution(
            description = "pr_step_9",
            expression = "L = ${fmt(vx)} \\cdot ${fmt(tFull)}",
            result = "L = ${fmt(range)}"
        )

        steps += SolutionStep.Formula(
            description = "max_h",
            expression = "H = h_0 + \\frac{v_{y0}^2}{2g}"
        )

        steps += SolutionStep.Substitution(
            description = "pr_step_9",
            expression = "H = ${fmt(h0)} + \\frac{${fmt(vy0)}^2}{2 \\cdot ${g}}",
            result = "H = ${fmt(hMax)}"
        )

        // результаты
        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("th_r", "L", range, "m"),
                PhysicalQuantity("max_h", "H", hMax, "m"),
                PhysicalQuantity("f_time", "t", tFull, "s")
            )
        )

        return steps
    }
}