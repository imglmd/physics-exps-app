package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ExampleExperiment : Experiment {

    override val id = "pendulum"
    override val name = "Математический маятник"
    override val category = "Механика"
    override val description =
        """Математический маятник — это модель тела, подвешенного на невесомой нити, которое колеблется под действием силы тяжести.""".trimIndent()
    override val imageRes = R.drawable.pendulum

    override val xLabel =  "Длина нити, м"
    override val yLabel = "Период, с"
    override val inputFields = listOf(
        InputField("length", "Длина нити", "L", "м", min = 0.0),
        InputField("period", "Период колебаний", "T", "с", min = 0.0),
        InputField("gravity", "Ускорение свободного падения", "g", "м/с²", min = 0.0),
        InputField(
            key = "angle",
            label = "Угол отклонения",
            symbol = "α",
            unit = "°",
            required = false,
            min = 0.0,
            max = 90.0
        )
    )

    override val minRequiredInputs = 2

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
        var amplitude: Double? = null
        var maxSpeed: Double? = null
        var maxHeight: Double? = null
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
            else -> {
                throw IllegalArgumentException("Нужно ввести две величины")
            }
        }
        frequency = 1 / period
        angularFrequency = sqrt(gravity/length)
        if (a != null) {
            val rad = a * ExpConstants.TO_RAD
            amplitude = length * sin(rad)
            maxSpeed = sqrt(2*gravity*length*(1 - cos(rad)))
            maxHeight = length*(1 - cos(rad))
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = buildList {
                add(PhysicalQuantity("Длина нити", "L", length, "м"))
                add(PhysicalQuantity("Период", "T", period, "с"))
                add(PhysicalQuantity("Ускорение", "g", gravity, "м/с²"))
                add(PhysicalQuantity("Частота колебаний", "V", frequency, "Гц"))
                add(PhysicalQuantity("Циклическая частота", "w₀", angularFrequency, "рад/с"))
                if (amplitude != null) {
                    add(PhysicalQuantity("Амплитуда", "A", amplitude, "м"))
                }
                if (maxSpeed != null) {
                    add(PhysicalQuantity("Максимальная скорость", "vₘₐₓ", maxSpeed, "м/с"))
                }
                if (maxHeight != null) {
                    add(PhysicalQuantity("Максимальная высота подъёма", "hₘₐₓ", maxHeight, "м"))
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
             title = "Идея решения",
             body = "Период в математическом маятнике зависит только от длины нити и ускорения свободного падения."
         )

        steps += SolutionStep.Formula(
            description = "Зная длину нити и ускорение свободного падения, найдём период колебаний.",
            expression = "T = 2\\pi\\sqrt{\\frac{l}{g}} "
        )

        steps += SolutionStep.Formula(
            description = "Зная длину нити и период колебаний, найдём ускорение свободного падения.",
            expression = "g = \\frac{4\\pi l}{T^2}"
        )

        steps += SolutionStep.Formula(
            description = "Зная период колебаний и ускорение свободного падения, найдём длину нити.",
            expression = "l = \\frac{g T^2}{4\\pi^2}"
        )

        steps += SolutionStep.Formula(
            description = "Зная период, вычислим линейную частоту колебаний",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Вычислим циклическую частоту - число колебаний за 2 \\pi секунд.",
            expression = "\\omega = \\sqrt{\\frac{g}{l}}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём амплитуду колебаний математического маятника.",
            expression = "A = l \\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "Найдём максимальную скорость математического маятника, она достигается " +
                    "в момент прохождения положения равновесия.",

            expression = "v_max = \\sqrt{2 g l (1 - \\cos(\\alpha))}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём максимальную высоту подъёма математического маятника, она " +
                    "достигается в крайних точках амплитуды колебаний, при этом скорость равна нулю",
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
                description = "Найдём период колебаний",
                expression = "T = 2\\pi\\sqrt{\\frac{$l}{$g}}",
                result = "T = ${fmt(T)} \\text{с}"
            )
        } else if(l != null && T != null) {
            g = (4 * PI.pow(2) * l) / T.pow(2)
            steps += SolutionStep.Substitution(
                description = "Найдём ускорение свободного падения",
                expression = "g = \\frac{4\\pi${l}}{${T}^2}",
                result = "g = ${fmt(g)} \\text{\\frac{м}{с^2}}"
            )
        } else if(T != null && g != null) {
            l = (g * T.pow(2)) / (4 * PI.pow(2))
            steps += SolutionStep.Substitution(
                description = "Найдём длину нити",
                expression = "l = \\frac{${g}${T}^2}{4\\pi^2}",
                result = "l = ${fmt(l)} \\text{м}"
            )
        }

        f = 1 / T!!
        aF = sqrt(g!!/ l!!)


        steps += SolutionStep.Substitution(
            description = "Найдём линейную частоту колебаний",
            expression = "\\nu = \\frac{1}{${fmt(T)}}",
            result = "\\nu = ${fmt(f)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Наёдём циклическую частоту",
            expression = "\\omega = \\sqrt{\\frac{${fmt(g)}}{${fmt(l)}}}",
            result = "\\omega = ${fmt(aF)} \\frac{рад}{с}"
        )

        if (a != null) {
            val rad = Math.toRadians(a)
            A = l * sin(rad)
            mV = sqrt(2*g*l*(1 - cos(rad)))
            mH = l*(1 - cos(rad))
            steps += SolutionStep.Substitution(
                description = "Найдём амплитуду колебаний математического маятника",
                expression = "A = ${fmt(l)} \\sin(${fmt(rad)})",
                result = "A = ${fmt(A)} \\text{м}"
            )

            steps += SolutionStep.Substitution(
                description = "Найдём максимальную скорость математического маятника",
                expression = "v_max = \\sqrt{2 \\times ${fmt(g)} \\times ${fmt(l)} \\times (1 - \\cos(${fmt(rad)}))}",
                result = "v_max = ${fmt(mV)} \\text{\\frac{м}{с}}"
            )

            steps += SolutionStep.Substitution(
                description = "Найдём максимальную высоту подъёма математического маятника",
                expression = "h_max = $l (1 - \\cos(${rad}))",
                result = "h_max = ${fmt(mH)} \\text{м}"
            )
            steps += SolutionStep.Result (
                listOf(
                    PhysicalQuantity("Длина нити", "L", l, "м"),
                    PhysicalQuantity("Период", "T", T, "с"),
                    PhysicalQuantity("Ускорение", "g", g, "м/с²"),
                    PhysicalQuantity("Частота колебаний", "V", f, "Гц"),
                    PhysicalQuantity("Циклическая частота", "w₀", aF, "рад/с"),
                    PhysicalQuantity("Амплитуда", "A", A, "м"),
                    PhysicalQuantity("Максимальная скорость", "vₘₐₓ", mV, "м/с"),
                    PhysicalQuantity("Максимальная высота подъёма", "hₘₐₓ", mH, "м")
                )
            )
        } else {
            steps += SolutionStep.Result (
                listOf(
                    PhysicalQuantity("Длина нити", "L", l, "м"),
                    PhysicalQuantity("Период", "T", T, "с"),
                    PhysicalQuantity("Ускорение", "g", g, "м/с²"),
                    PhysicalQuantity("Частота колебаний", "V", f, "Гц"),
                    PhysicalQuantity("Циклическая частота", "w₀", aF, "рад/с"),
                )
            )
        }

        return steps
    }
}