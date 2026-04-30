package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import kotlin.math.ln
import kotlin.math.pow

class RadioactiveDecay: Experiment {
    override val id = "radioactive_decay"
    override val name = "Радиоактивный распад"
    override val category = "Ядерная физика"
    override val description = ""
    override val xLabel =  "Время полураспада, с"
    override val yLabel = "Количество оставшихся радиоактивных ядер"
    override val inputFields = listOf(
        InputField("start_count", "Начальное число радиоактивных ядер", "N₀", "", required = true, min = 0.0),
        InputField("period", "Период полураспада", "T", "с", required = true, min = 0.0),
        InputField("time", "Время от начала распада", "t", "с", required = true, min = 0.0)
    )
    override val minRequiredInputs = 3

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
                PhysicalQuantity("Начальное число ядер", "N₀", N0, ""),
                PhysicalQuantity("Период полураспада", "T", period, "с"),
                PhysicalQuantity("Время от начала распада", "t", time, "с"),
                PhysicalQuantity("Количество оставшихся ядер", "N",
                    N, ""),
                PhysicalQuantity("Постоянная радиоактивного распада", "λ",
                    constRadioactiveDecay, "с⁻¹"),
                PhysicalQuantity("Среднее время жизни", "τ", srTime, "с")
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
            title = "Идея решения",
            body = "Использование закона радиоактивного распада, описывающего зависимость " +
                    "интенсивности радиоактивного распада от времени и от количества радиоактивных " +
                    "атомов в образце."
        )

        steps += SolutionStep.Formula(
            description = "Найдём количество оставшихся ядер, используя закон радиоактивного распада.",
            expression = "N = N_0 2^\\frac{-t}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём постоянную радиоактивного распада - величина, определяющая " +
                    "вероятность распада нестабильного атомного ядра за единицу времени.",
            expression = "\\lambda = \\frac{ln2}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём среднее время жизни - средний промежуток времени, в течение " +
                    "которого ядро остается радиоактивным до распада",
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
            description = "Найдём количество оставшихся ядер",
            expression = "N = $N0 \\times 2^\\frac{-$t}{$T})",
            result = "N = ${fmt(N)}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём постоянную радиоактивного распада",
            expression = "\\lambda = \\frac{ln2}{$T}",
            result = "\\lambda = ${fmt(constRadioactiveDecay)} \\text{с^-1}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём среднее время жизни",
            expression = "\\tau = \\frac{1}{$T}",
            result = "\\tau = ${fmt(srTime)} \\text{с}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("Количество оставшихся ядер", "N",
                    N, ""),
                PhysicalQuantity("Постоянная радиоактивного распада", "λ",
                    constRadioactiveDecay, "с⁻¹"),
                PhysicalQuantity("Среднее время жизни", "τ", srTime, "с")
            )
        )

        return steps
    }
}