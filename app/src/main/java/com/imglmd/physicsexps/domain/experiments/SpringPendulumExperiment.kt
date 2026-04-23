package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.PI
import kotlin.math.sqrt

class SpringPendulumExperiment: Experiment {
    override val id = "spring_pendulum"
    override val name = "Пружинный маятник"
    override val category = "Механика"
    override val description = "Пружинный маятник — это механическая колебательная система, состоящая из груза определённой массы , прикрепленного к упругой пружине с коэффицентом упругости, другой конец которой жестко закреплен. Он совершает колебания под действием силы упругости около положения равновесия."
    override val inputFields = listOf(
        InputField("weight", "Масса", "m", "кг"),
        InputField("coeff", "Коэффицент упругости пружины", "k", "Н/м")
    )
    override val xLabel = "Масса груза, кг"
    override val yLabel = "Период, с"

    override val minRequiredInputs = 2

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val m = inputs["weight"]
        val k = inputs["coeff"]

        val T: Double
        val map = mutableMapOf<String, Double>()

        when {
            m != null && k != null -> {
                T = 2 * PI * sqrt(m/k)
                map.put("weight", m)
                map.put("coeff", k)
            }
            else -> throw IllegalArgumentException("Нужно ввести две величины")
        }

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Период", "T", T, "с"),
                PhysicalQuantity("Масса", "m", m, "кг"),
                PhysicalQuantity("Коэффицент упругости", "k", k, "Н/м")
            ),
            points = getPoints(map),
            date = "${LocalDate.now()}",
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val m = inputs.getValue("weight")
        val k = inputs.getValue("coeff")
        val startX = 0.0
        val step = m / 100.0

        var x = startX
        while (x <= m + step) {
            val y = 2 * PI * sqrt(x/k)
            list.add(Pair(x, y))
            x += step
        }
        return list
    }
}