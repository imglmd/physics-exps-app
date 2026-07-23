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
import kotlin.math.pow
import kotlin.math.sqrt

class SpringPendulumExperiment: Experiment {
    override val id = "spring_pendulum"
    override val category = "mechanics"
    override val description = "spring_pendulum_desc"

    override val inputFields = listOf(
        InputField("weight", "weight", "m", "kg", min = 0.0),
        InputField("coeff", "spring_constant", "k", "n_m", min =0.0),
        InputField("period", "period_o", "T", "s", min = 0.0)
    )
    override val xLabel = "Масса груза, кг"
    override val yLabel = "period_s"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val weight = inputs["weight"]
        val coeff = inputs["coeff"]
        val period = inputs["period"]

        val errors = mutableListOf<ValidationError>()
        val count = listOf(weight, coeff, period).count { it != null }

        when {
            count < 2 -> {
                errors += ValidationError.NotEnoughInputs
            }

            count > 2 -> {
                errors += ValidationError.InvalidCombination
            }
        }

        if (weight != null && weight <= 0.0) errors += ValidationError.OutOfRange("weight", min = 0.01)
        if (coeff != null && coeff <= 0.0) errors += ValidationError.OutOfRange("coeff", min = 0.01)
        if (period != null && period <= 0.0) errors += ValidationError.OutOfRange("period", min = 0.01)


        return if (errors.isEmpty()) {
            ValidationResult.Success(inputs)
        } else {
            ValidationResult.Error(errors)
        }
    }

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val m = inputs["weight"]
        val k = inputs["coeff"]
        val T = inputs["period"]

        val period: Double
        val frequency: Double
        val coeff: Double
        val weight: Double
        val angularFrequency: Double
        val map = mutableMapOf<String, Double>()

        when {
            m != null && k != null -> {
                period = 2 * PI * sqrt(m/k)
                weight = m
                coeff = k
                frequency = 1 / period
                angularFrequency = sqrt(coeff/weight)
                map.put("weight", weight)
                map.put("coeff", coeff)
            }

            m != null && T != null -> {
                coeff = (4*PI*PI*m)/T*T
                weight = m
                period = T
                frequency = 1 / period
                angularFrequency = sqrt(coeff/weight)
                map.put("weight", weight)
                map.put("coeff", coeff)
            }

            k != null && T != null -> {
                weight = k * ((T/2* PI).pow(2))
                coeff = k
                period = T
                frequency = 1 / period
                angularFrequency = sqrt(coeff/weight)
                map.put("weight", weight)
                map.put("coeff", coeff)
            }
            else -> throw IllegalArgumentException("Нужно ввести две величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("per", "T", period, "s"),
                PhysicalQuantity("weight", "m", weight, "kg"),
                PhysicalQuantity("spring_constant", "k", coeff, "n_m"),
                PhysicalQuantity("o_freq", "V", frequency, "hz"),
                PhysicalQuantity("ang_f", "w₀", angularFrequency, "rad_s")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val m = inputs.getValue("weight")
        val k = inputs.getValue("coeff")
        val startX = 0.0
        val step = m / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while (x <= m + step) {
            val y = 2 * PI * sqrt(x/k)
            list.add(Pair(x, y))
            x += step
        }
        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "sp_step_1"

        )

        steps += SolutionStep.Formula(
            description = "sp_step_2",
            expression = "T = 2 \\pi \\sqrt{\\frac{m}{k}}"
        )

        steps += SolutionStep.Formula(
            description = "sp_step_3",
            expression = "k = \\frac{4 \\pi^2 m}{T^2}"
        )

        steps += SolutionStep.Formula(
            description = "sp_step_4",
            expression = "m = k (\\frac{T}{2 \\pi})^2"
        )

        steps += SolutionStep.Formula(
            description = "sp_step_5",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "sp_step_6",
            expression = "\\omega = \\sqrt{\\frac{k}{m}}"
        )

        if (inputs == null) return steps

        var weight = inputs["weight"]
        var coeff = inputs["coeff"]
        var period = inputs["period"]

        val frequency: Double
        val angularFrequency: Double

        val fmt = { d: Double -> "%.2f".format(d) }

        if (weight != null && coeff != null) {
            period = 2 * PI * sqrt(weight/coeff)

            steps += SolutionStep.Substitution(
                description = "sp_step_7",
                expression = "T = 2 \\pi \\sqrt{\\frac{${fmt(weight)}}{${fmt(coeff)}}}",
                result = "T = ${fmt(period)}"
            )

        }else if (weight != null && period != null) {
            coeff = (4*PI*PI*weight)/period*period

            steps += SolutionStep.Substitution(
                description = "sp_step_8",
                expression = "k = \\frac{4 \\pi^2 ${fmt(weight)}}{${fmt(period)}^2}",
                result = "k = ${fmt(coeff)}"
            )

        } else if (coeff != null && period != null) {
            weight = coeff * ((period/2* PI).pow(2))

            steps += SolutionStep.Substitution(
                description = "sp_step_9",
                expression = "m = ${fmt(coeff)} (\\frac{${fmt(period)}}{2 \\pi})^2",
                result = "m = ${fmt(weight)}"
            )
        }

        frequency = 1 / period!!
        angularFrequency = sqrt(coeff!!/weight!!)

        steps += SolutionStep.Substitution(
            description = "math_step_13",
            expression = "\\nu = \\frac{1}{${fmt(period)}}",
            result = "\\nu = ${fmt(frequency)}"
        )

        steps += SolutionStep.Substitution(
            description = "har_step_5",
            expression = "\\omega = \\sqrt{\\frac{${fmt(coeff)}}{${fmt(weight)}}}",
            result = "\\omega = ${fmt(angularFrequency)}"
        )

        steps += SolutionStep.Result(
            quantities = listOf(
                PhysicalQuantity("per", "T", period, "s"),
                PhysicalQuantity("weight", "m", weight, "kg"),
                PhysicalQuantity("spring_constant", "k", coeff, "n_m"),
                PhysicalQuantity("o_freq", "V", frequency, "hz"),
                PhysicalQuantity("ang_f", "w₀", angularFrequency, "rad_s")
            )
        )
        return steps
    }
}