package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import kotlin.math.ln
import kotlin.math.pow

class RadioactiveDecayExperiment: Experiment {
    override val id = "radioactive_decay"
    override val category = "nuclear_physics"
    override val description = "radioactive_decay_desc"
    override val xLabel =  "half_life"
    override val yLabel = "numb"
    override val inputFields = listOf(
        InputField("start_count", "in_count", "N₀", "", required = true, min = 0.0),
        InputField("period", "half_life_", "T", "s", required = true, min = 0.0),
        InputField("time", "decay_time", "t", "s", required = true, min = 0.0)
    )


    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val N0 = inputs["start_count"]
        val T = inputs["period"]
        val t = inputs["time"]

        val N: Double
        val constRadioactiveDecay: Double
        val time: Double
        val period: Double
        val srTime: Double
        val map = mutableMapOf<String, Double>()

        when {
            N0 != null && T != null && t != null -> {
                N = N0 * 2.0.pow(-t/T)
                period = T
                time = t
                constRadioactiveDecay = ln(2.0)/period
                srTime = 1 / constRadioactiveDecay
                map.put("time", t)
                map.put("start_count", N0)
                map.put("period", T)
            }
            else ->
                throw IllegalArgumentException("Нужно ввести три величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("in_count", "N₀", N0, ""),
                PhysicalQuantity("half_life_", "T", period, "s"),
                PhysicalQuantity("decay_time", "t", time, "s"),
                PhysicalQuantity("nmb_rem", "N",
                    N, ""),
                PhysicalQuantity("rad_const", "λ",
                    constRadioactiveDecay, "s_1"),
                PhysicalQuantity("av_life", "τ", srTime, "s")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val t: Double = inputs.getValue("time")
        val T: Double = inputs.getValue("period")
        val N0: Double = inputs.getValue("start_count")

        val startX = 0.0
        val step = t / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while (x <= t + step) {
            val y = N0 * 2.0.pow(-x/T)
            list.add(Pair(x, y))
            x += step
        }
        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "dec_step_1"
        )

        steps += SolutionStep.Formula(
            description = "dec_step_2",
            expression = "N = N_0 2^\\frac{-t}{T}"
        )

        steps += SolutionStep.Formula(
            description = "dec_step_3",
            expression = "\\lambda = \\frac{ln2}{T}"
        )

        steps += SolutionStep.Formula(
            description = "dec_step_4",
            expression = "\\tau = \\frac{1}{T}"
        )

        if (inputs == null) return steps

        val N0 = inputs.getValue("start_count")
        val T = inputs.getValue("period")
        val t = inputs.getValue("time")

        val N: Double
        val constRadioactiveDecay: Double
        val srTime: Double

        N = N0 * 2.0.pow(-t/T)
        constRadioactiveDecay = ln(2.0)/T
        srTime = 1 / T

        val fmt = { d: Double -> "%.2f".format(d) }

        steps += SolutionStep.Substitution(
            description = "dec_step_5",
            expression = "N = $N0 \\times 2^\\frac{-$t}{$T})",
            result = "N = ${fmt(N)}"
        )

        steps += SolutionStep.Substitution(
            description = "dec_step_6",
            expression = "\\lambda = \\frac{ln2}{$T}",
            result = "\\lambda = ${fmt(constRadioactiveDecay)}"
        )

        steps += SolutionStep.Substitution(
            description = "dec_step_7",
            expression = "\\tau = \\frac{1}{$T}",
            result = "\\tau = ${fmt(srTime)}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("nmb_rem", "N", N, ""),
                PhysicalQuantity("rad_const", "λ", constRadioactiveDecay, "s_1"),
                PhysicalQuantity("av_life", "τ", srTime, "s")
            )
        )

        return steps
    }
}