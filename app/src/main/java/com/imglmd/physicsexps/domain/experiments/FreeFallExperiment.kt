package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import kotlin.math.pow

class FreeFallExperiment: Experiment {
    override val id = "free_fall"
    override val category = "kinematics"
    override val description = "free_fall_desc"
    override val inputFields = listOf(
        InputField("start_speed", "initial_velocity", "v₀", "m_s", required = false),
        InputField("duration", "body_dur", "t", "s", required = true, min = 0.0)
    )
    override val xLabel =  "time"
    override val yLabel = "у"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val time = inputs["duration"] ?: return ValidationResult.Error(
            listOf(ValidationError.NotEnoughInputs)
        )

        if (time == 0.0) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }

        return ValidationResult.Success(inputs)
    }

    override fun calculate(
        inputs: Map<String, Double>
    ): ExperimentResult {

        val v0 = inputs["start_speed"] ?: 0.0
        val t = inputs.getValue("duration")

        val g = ExpConstants.GRAVITY

        val v = v0 + g * t
        val h = v0 * t + (g * t.pow(2)) / 2

        val map = mapOf(
            "start_speed" to v0,
            "duration" to t
        )

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("initial_velocity", "v₀",v0, "m_s"),
                PhysicalQuantity("body_dur", "t", t, "s"),
                PhysicalQuantity(
                    label = "v_moment",
                    symbol = "v",
                    value = v,
                    unit = "m_s"
                ),
                PhysicalQuantity(
                    label = "dist_tr",
                    symbol = "h",
                    value = h,
                    unit = "m"
                )
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val v0 = inputs.getValue("start_speed")
        val t = inputs.getValue("duration")
        val g = ExpConstants.GRAVITY

        val startX = 0.0
        val step = t / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while(x <= t + step) {
            val y = v0 * x + ((g*x.pow(2))/2)
            list.add(Pair(x, y))
            x += step
        }

        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "free_step_1"
        )

        steps += SolutionStep.Formula(
            description = "free_step_2",
            expression = "v = v_0 + gt"
        )

        steps += SolutionStep.Formula(
            description = "free_step_3",
            expression = "h = v_0t + \\frac{g t^2}{2}"
        )

        if (inputs == null) return steps

        val v0 = inputs.getValue("start_speed")
        val t = inputs.getValue("duration")
        val g = ExpConstants.GRAVITY
        val v: Double
        val h: Double

        v = v0 + g*t
        h = v0*t + (g * t.pow(2)) / 2

        val fmt = {d: Double -> "%.2f".format(d)}

        steps += SolutionStep.Substitution(
            description = "free_step_2",
            expression = "v = $v0 + $g \\times $t",
            result = "v = ${fmt(v)}"
        )

        steps += SolutionStep.Substitution(
            description = "free_step_3",
            expression = "h = $v0 \\times $t + \\frac{$g \\times $t^2}{2}",
            result = "h = ${fmt(h)}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity(
                    label = "v_moment",
                    symbol = "v", v, "m_s"
                ),
                PhysicalQuantity(
                    label = "dist_tr",
                    symbol = "h", h, "m"
                )
            )
        )
        return steps
    }
}