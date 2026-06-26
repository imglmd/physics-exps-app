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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class PendulumExperiment : Experiment {

    override val id = "pendulum"
    override val name = "pendulum"
    override val category = "mechanics"
    override val description = "pendulum_desc"
    override val imageRes = R.drawable.mathpendulum

    override val xLabel =  "thread"
    override val yLabel = "period_s"
    override val inputFields = listOf(
        InputField("length", "thread_length", "L", "m", min = 0.0),
        InputField("period", "period_o", "T", "s", min = 0.0),
    )

    override val additionalInputFields = listOf(
        InputField("gravity", "a_gr", "g", "m_s_2", min = 0.0),
        InputField(
            key = "angle",
            label = "def_ang",
            symbol = "α",
            unit = "ang",
            required = false,
            min = 0.0,
            max = 90.0
        )
    )

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val length = inputs["length"]
        val period = inputs["period"]
        val gravity = inputs["gravity"]
        val angle = inputs["angle"]

        val errors = mutableListOf<ValidationError>()

        val count = listOf(length, period, gravity).count { it != null }

        when {
            count < 2 -> {
                errors += ValidationError.NotEnoughInputs
            }
            count > 2 -> {
                errors += ValidationError.InvalidCombination
            }
        }

        if (length != null && length <= 0.0) {
            errors += ValidationError.OutOfRange(
                fieldKey = "length",
                min = 0.01
            )
        }

        if (period != null && period <= 0.0) {
            errors += ValidationError.OutOfRange(
                fieldKey = "period",
                min = 0.01
            )
        }

        if (gravity != null && gravity <= 0.0) {
            errors += ValidationError.OutOfRange(
                fieldKey = "gravity",
                min = 0.01
            )
        }

        if (angle != null && (angle !in 0.0..90.0)) {
            errors += ValidationError.OutOfRange(
                fieldKey = "angle",
                min = 0.0,
                max = 90.0
            )
        }

        return if (errors.isEmpty()) {
            ValidationResult.Success(inputs)
        } else {
            ValidationResult.Error(errors)
        }
    }

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
        var amplitude: Double?
        var maxSpeed: Double?
        var maxHeight: Double?
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
            else -> error("")
        }
        frequency = 1 / period
        angularFrequency = sqrt(gravity/length)
        if (a != null) {
            val rad = a * ExpConstants.TO_RAD
            amplitude = length * sin(rad)
            maxSpeed = sqrt(2*gravity*length*(1 - cos(rad)))
            maxHeight = length*(1 - cos(rad))
        } else {
            amplitude = null
            maxSpeed = null
            maxHeight = null
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = buildList {
                add(PhysicalQuantity("thread_length", "L", length, "m"))
                add(PhysicalQuantity("period_o", "T", period, "s"))
                add(PhysicalQuantity("acceleration", "g", gravity, "m_s_2"))
                add(PhysicalQuantity("osc_fr", "ν", frequency, "hz"))
                add(PhysicalQuantity("ang_f", "w₀", angularFrequency, "rad_s"))
                if (a != null) {
                    add(PhysicalQuantity("def_ang", "α", a, "ang"))
                }
                if (amplitude != null) {
                    add(PhysicalQuantity("amp", "A", amplitude, "m"))
                }
                if (maxSpeed != null) {
                    add(PhysicalQuantity("max_speed", "vₘₐₓ", maxSpeed, "m_s"))
                }
                if (maxHeight != null) {
                    add(PhysicalQuantity("max_height", "hₘₐₓ", maxHeight, "m"))
                }
            },
            points = getPoints(map),
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

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()
         steps += SolutionStep.Theory(
             title = "solution_idea",
             body = "math_step_1"
         )

        steps += SolutionStep.Formula(
            description = "math_step_2",
            expression = "T = 2\\pi\\sqrt{\\frac{l}{g}}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_3",
            expression = "g = \\frac{4\\pi l}{T^2}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_4",
            expression = "l = \\frac{g T^2}{4\\pi^2}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_5",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_6",
            expression = "\\omega = \\sqrt{\\frac{g}{l}}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_7",
            expression = "A = l \\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "math_step_8",

            expression = "v_max = \\sqrt{2 g l (1 - \\cos(\\alpha))}"
        )

        steps += SolutionStep.Formula(
            description = "math_step_9",
            expression = "h_max = l (1 - \\cos(\\alpha))"
        )

        if (inputs == null) return steps

        var l = inputs["length"]
        var g = inputs["gravity"]
        var T = inputs["period"]
        val a = inputs["angle"]

        val f: Double
        val aF: Double
        val A: Double
        val mV: Double
        val mH: Double

        val fmt = { d: Double -> "%.2f".format(d) }

        if (l != null && g != null) {
            T = 2 * PI * sqrt(l / g)
            steps += SolutionStep.Substitution(
                description = "math_step_10",
                expression = "T = 2\\pi\\sqrt{\\frac{$l}{$g}}",
                result = "T = ${fmt(T)}"
            )
        } else if(l != null && T != null) {
            g = (4 * PI.pow(2) * l) / T.pow(2)
            steps += SolutionStep.Substitution(
                description = "math_step_11",
                expression = "g = \\frac{4\\pi${l}}{${T}^2}",
                result = "g = ${fmt(g)}"
            )
        } else if(T != null && g != null) {
            l = (g * T.pow(2)) / (4 * PI.pow(2))
            steps += SolutionStep.Substitution(
                description = "math_step_12",
                expression = "l = \\frac{${g}${T}^2}{4\\pi^2}",
                result = "l = ${fmt(l)}"
            )
        }

        f = 1 / T!!
        aF = sqrt(g!!/ l!!)


        steps += SolutionStep.Substitution(
            description = "math_step_13",
            expression = "\\nu = \\frac{1}{${fmt(T)}}",
            result = "\\nu = ${fmt(f)}"
        )

        steps += SolutionStep.Substitution(
            description = "math_step_14",
            expression = "\\omega = \\sqrt{\\frac{${fmt(g)}}{${fmt(l)}}}",
            result = "\\omega = ${fmt(aF)}"
        )

        if (a != null) {
            val rad = Math.toRadians(a)
            A = l * sin(rad)
            mV = sqrt(2*g*l*(1 - cos(rad)))
            mH = l*(1 - cos(rad))
            steps += SolutionStep.Substitution(
                description = "math_step_15",
                expression = "A = ${fmt(l)} \\sin(${fmt(a)})",
                result = "A = ${fmt(A)}"
            )

            steps += SolutionStep.Substitution(
                description = "math_step_16",
                expression = "v_max = \\sqrt{2 \\times ${fmt(g)} \\times ${fmt(l)} \\times (1 - \\cos(${fmt(a)}))}",
                result = "v_max = ${fmt(mV)}"
            )

            steps += SolutionStep.Substitution(
                description = "math_step_17",
                expression = "h_max = $l (1 - \\cos(${fmt(a)}))",
                result = "h_max = ${fmt(mH)}"
            )
            steps += SolutionStep.Result (
                listOf(
                    PhysicalQuantity("thread_length", "L", l, "m"),
                    PhysicalQuantity("period_o", "T", T, "s"),
                    PhysicalQuantity("acceleration", "g", g, "m_s_2"),
                    PhysicalQuantity("osc_fr", "V", f, "hz"),
                    PhysicalQuantity("ang_f", "w₀", aF, "rad_s"),
                    PhysicalQuantity("amp", "A", A, "м"),
                    PhysicalQuantity("max_speed", "vₘₐₓ", mV, "m_s"),
                    PhysicalQuantity("max_height", "hₘₐₓ", mH, "m")
                )
            )
        } else {
            steps += SolutionStep.Result (
                listOf(
                    PhysicalQuantity("thread_length", "L", l, "m"),
                    PhysicalQuantity("period_o", "T", T, "s"),
                    PhysicalQuantity("acceleration", "g", g, "m_s_2"),
                    PhysicalQuantity("osc_fr", "V", f, "hz"),
                    PhysicalQuantity("ang_f", "w₀", aF, "rad_s"),
                )
            )
        }

        return steps
    }
}