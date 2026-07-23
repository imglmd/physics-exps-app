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
import kotlin.math.asin
import kotlin.math.sin

class HarmonicVibrationsExperiment: Experiment {
    override val id = "harmonic_vibrations"
    override val category = "mechanics"
    override val description = "harmonic_vibrations_desc"
    override val xLabel = "time"
    override val yLabel = "disp"
    override val inputFields = listOf(
        InputField("period", "period_o","T", "s", min = 0.00001, required = true ),
        InputField("amplitude", "amp", "A", "m", required = true, min = 0.00001),
        InputField("start_position", "start_c", "x₀", "m",required = true),
        InputField("time", "dur_o", "t", "s", min = 0.0, required = true)
    )

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val amplitude = inputs["amplitude"]!!
        val startPosition = inputs["start_position"]!!

        if (abs(startPosition) > amplitude) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }
        return ValidationResult.Success(inputs)
    }

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val A = inputs.getValue("amplitude")
        val T = inputs.getValue("period")
        val x_0 = inputs.getValue("start_position")
        val t = inputs.getValue("time")
        val map = mutableMapOf<String, Double>()

        val angularFrequency = (2 * PI)/T
        val phase = asin(x_0/A)

        map.put("amplitude", A)
        map.put("angular_frequency",angularFrequency)
        map.put("phase", phase)
        map.put("start_position", x_0)
        map.put("time", t)

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("period_o", "T", T, "s"),
                PhysicalQuantity("amp", "A", A, "m"),
                PhysicalQuantity("start_c", "x₀", x_0, "m"),
                PhysicalQuantity("ang_f", "ω", angularFrequency, "rad_s"),
                PhysicalQuantity("in_ph", "φ", phase, "rad"),
                PhysicalQuantity("dur_o", "t", t, "s")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val angularFrequency = inputs.getValue("angular_frequency")
        val phase = inputs.getValue("phase")
        val amplitude = inputs.getValue("amplitude")
        val t = inputs.getValue("time")
        val startX = 0.0
        val step = t / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while (x <= t + step) {
            val y = amplitude * sin(angularFrequency*x + phase)
            list.add(Pair(x, y))
            x += step
        }
        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()
        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "har_step_1"
        )

        steps += SolutionStep.Formula(
            description = "har_step_2",
            expression = "\\omega = \\frac{2\\pi}{T}"
        )

        steps += SolutionStep.Formula(
            description = "har_step_3",
            expression = "\\phi = arcsin(\\frac{x_0}{A})"
        )

        steps += SolutionStep.Formula(
            description = "har_step_4",
            expression = "x(t) = A \\sin(\\omega t + \\phi)\\\\ x(t) = A \\cos(\\omega t + \\phi)"
        )

        if (inputs == null) return steps

        val A = inputs.getValue("amplitude")
        val T = inputs.getValue("period")
        val x_0 = inputs.getValue("start_position")
        val t = inputs.getValue("time")

        val angularFrequency = (2 * PI)/T
        val phase = asin(x_0/A)

        val fmt = { d: Double -> "%.2f".format(d) }

        steps += SolutionStep.Substitution(
            description = "har_step_5",
            expression = "\\omega = \\frac{2\\pi}{$T}",
            result = "\\omega = ${fmt(angularFrequency)}"
        )

        steps += SolutionStep.Substitution(
            description = "har_step_6",
            expression = "\\phi = arcsin(\\frac{$x_0}{$A})",
            result = "\\phi = ${fmt(phase)}"
        )

        steps += SolutionStep.Substitution(
            description = "har_step_4",
            expression = "x(t) = A \\sin(${fmt(angularFrequency)} \\times t + ${fmt(phase)})\\\\ " +
                    "x(t) = A \\cos(${fmt(angularFrequency)} \\times t + ${fmt(phase)})",
            result = ""
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("ang_f", "ω", angularFrequency, "rad_s"),
                PhysicalQuantity("in_ph", "φ", phase, "rad"),
                PhysicalQuantity("dur_o", "t", t, "s")
            )
        )
        return steps
    }
}