package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class SpringPendulumExperiment: Experiment {
    override val id = "spring_pendulum"
    override val name = "Пружинный маятник"
    override val category = "Механика"
    override val description = "Пружинный маятник — это механическая колебательная система, состоящая из груза определённой массы , прикрепленного к упругой пружине с коэффицентом упругости, другой конец которой жестко закреплен. Он совершает колебания под действием силы упругости около положения равновесия."
    override val imageRes = R.drawable.spring_pendulum

    override val inputFields = listOf(
        InputField("weight", "Масса", "m", "кг", min = 0.0),
        InputField("coeff", "Коэффицент упругости пружины", "k", "Н/м", min =0.0),
        InputField("period", "Период колебаний", "T", "с", min = 0.0)
    )
    override val xLabel = "Масса груза, кг"
    override val yLabel = "Период, с"

    override val minRequiredInputs = 2

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
                PhysicalQuantity("Период", "T", period, "с"),
                PhysicalQuantity("Масса", "m", weight, "кг"),
                PhysicalQuantity("Коэффицент упругости", "k", coeff, "Н/м"),
                PhysicalQuantity("Частота колебаний", "V", frequency, "Гц"),
                PhysicalQuantity("Циклическая частота", "w₀", angularFrequency, "рад/с")
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
            title = "Идея решения",
            body = "Период колебаний пружинного маятника зависит от массы груза и жёсткости пружины." +
                    " Колебания груза происходят под действием силы упругости пружины."

        )

        steps += SolutionStep.Formula(
            description = "Зная массу груза и коэффицент жёсткости пружины, найдём период колебаний.",
            expression = "T = 2 \\pi \\sqrt{\\frac{m}{k}}"
        )

        steps += SolutionStep.Formula(
            description = "Зная массу груза и период колебаний, найдём коэффицент жёсткости.",
            expression = "k = \\frac{4 \\pi^2 m}{T^2}"
        )

        steps += SolutionStep.Formula(
            description = "Зная период колебаний и коэффицент жёсткости, найдём массу груза.",
            expression = "m = k (\\frac{T}{2 \\pi})^2"
        )

        steps += SolutionStep.Formula(
            description = "Зная период, вычислим линейную частоту колебаний",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Вычислим циклическую частоту - число колебаний за 2 π секунд.",
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
                description = "Найдём период колебаний",
                expression = "T = 2 \\pi \\sqrt{\\frac{${fmt(weight)}}{${fmt(coeff)}}}",
                result = "T = ${fmt(period)} \\text{с}"
            )

        }else if (weight != null && period != null) {
            coeff = (4*PI*PI*weight)/period*period

            steps += SolutionStep.Substitution(
                description = "Найдём коэффицент жёсткости",
                expression = "k = \\frac{4 \\pi^2 ${fmt(weight)}}{${fmt(period)}^2}",
                result = "k = ${fmt(coeff)} \\text{Н/м}"
            )

        } else if (coeff != null && period != null) {
            weight = coeff * ((period/2* PI).pow(2))

            steps += SolutionStep.Substitution(
                description = "Найдём массу груза",
                expression = "m = ${fmt(coeff)} (\\frac{${fmt(period)}}{2 \\pi})^2",
                result = "m = ${fmt(weight)} \\text{кг}"
            )
        }

        frequency = 1 / period!!
        angularFrequency = sqrt(coeff!!/weight!!)

        steps += SolutionStep.Substitution(
            description = "Найдём линейную частоту колебаний",
            expression = "\\nu = \\frac{1}{${fmt(period)}}",
            result = "\\nu = ${fmt(frequency)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Наёдём циклическую частоту",
            expression = "\\omega = \\sqrt{\\frac{${fmt(coeff)}}{${fmt(weight)}}}",
            result = "\\omega = ${fmt(angularFrequency)} \\frac{рад}{с}"
        )

        steps += SolutionStep.Result(
            quantities = listOf(
                PhysicalQuantity("Период", "T", period, "с"),
                PhysicalQuantity("Масса", "m", weight, "кг"),
                PhysicalQuantity("Коэффицент упругости", "k", coeff, "Н/м"),
                PhysicalQuantity("Частота колебаний", "V", frequency, "Гц"),
                PhysicalQuantity("Циклическая частота", "w₀", angularFrequency, "рад/с")
            )
        )
        return steps
    }
}