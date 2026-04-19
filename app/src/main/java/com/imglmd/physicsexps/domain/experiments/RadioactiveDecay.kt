package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.pow

class RadioactiveDecay: Experiment {
    override val id = "radioactive_decay"
    override val name = "Радиоактивный распад"
    override val category = "Ядерная физика"
    override val description = ""
    override val inputFields = listOf(
        InputField("start_count", "Начальное число радиоактивных ядер", "N₀", ""),
        InputField("period", "Период полураспада", "T", "с"),
        InputField("time", "Промежутой времени от начала распада", "t", "с")
    )
    override val minRequiredInputs = 3

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val N0 = inputs["start_count"]
        val T = inputs["period"]
        val t = inputs["time"]
        val N: Double
        val map = mutableMapOf<String, Double>()

        when {
            N0 != null && T != null && t != null -> {
                N = N0 * 2.0.pow(-t/T)
            }
            else ->
                throw IllegalArgumentException("Нужно ввести три величины")
        }

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Количество оставшихся радиоактивных ядер", "N",
                    N, "")
            ),
            points = getPoints(map),
            date = "${LocalDate.now()}"

        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        TODO("Not yet implemented")
    }
}