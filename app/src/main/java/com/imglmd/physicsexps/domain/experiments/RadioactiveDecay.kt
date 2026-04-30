package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
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
        InputField("start_count", "Начальное число радиоактивных ядер", "N₀", ""),
        InputField("period", "Период полураспада", "T", "с"),
        InputField("time", "Время от начала распада", "t", "с")
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
}