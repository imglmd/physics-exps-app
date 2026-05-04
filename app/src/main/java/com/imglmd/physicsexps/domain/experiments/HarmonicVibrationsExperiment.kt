package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.presentation.navigation.Screen
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.exp
import kotlin.math.sin

class HarmonicVibrationsExperiment: Experiment {
    override val id = "harmonic_vibrations"
    override val name = "Гармонические колебания"
    override val category = "Механика"
    override val description = " колебания, при которых " +
            "некоторая физическая величина изменяется с " +
            "течением времени по гармоническому (синусоидальному, косинусоидальному) закону."
    override val xLabel = "Время, с"
    override val yLabel = "Смещение, м"
    override val inputFields = listOf(
        InputField("period", "Период колебаний","T", "с", min = 0.0, required = true ),
        InputField("amplitude", "Амплитуда", "A", "м", required = true, min = 0.0),
        InputField("start_position", "Началтная координата", "x₀", "м",required = true),
        InputField("time", "Продолжительность колебаний", "t", "с", min = 0.0, required = true)
    )
    override val minRequiredInputs = 2

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
                PhysicalQuantity("Период колебаний", "T", T, "с"),
                PhysicalQuantity("Амплитуда", "A", A, "м"),
                PhysicalQuantity("Начальная координата", "x₀", x_0, "м"),
                PhysicalQuantity("Циклическая частота", "ω", angularFrequency, "рад/с"),
                PhysicalQuantity("Начальная фаза", "φ", phase, "рад"),
                PhysicalQuantity("Продолжительность колебаний", "t", t, "с")
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
            title = "Идея решения",
            body = "Уравнение гармонических колебаний описывает осциллирующее движение физической величины по гармоническому закону, чтобы перейти к " +
                    "гармоническим колебаниям, нам нужно описать величины, которые помогут нам" +
                    " эти колебания охарактеризовать. Любое колебательное движение можно" +
                    " описать величинами: период, частота, амплитуда, фаза колебаний."
        )

        steps += SolutionStep.Formula(
            description = "Найдём циклическую частоту - физическая величина, равная числу полных колебаний, совершаемых за 2π секунд",
            expression = "\\omega = \\frac{2\\pi}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём начальную фазу колебаний - физическая величина, которая показывает отклонение точки от положения равновесия.",
            expression = "\\phi = arcsin(\\frac{x_0}{A})"
        )

        steps += SolutionStep.Formula(
            description = "Запишем уравнение гармонических колебаний.",
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
            description = "Найдём циклическую частоту",
            expression = "\\omega = \\frac{2\\pi}{$T}",
            result = "\\omega = ${fmt(angularFrequency)} \\text{рад/с}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём начальную фазу колебаний",
            expression = "\\phi = arcsin(\\frac{$x_0}{$A})",
            result = "\\phi = ${fmt(phase)}"
        )

        steps += SolutionStep.Substitution(
            description = "Запишем уравнение гармонических колебаний",
            expression = "x(t) = A \\sin(${fmt(angularFrequency)} \\times t + ${fmt(phase)})\\\\ " +
                    "x(t) = A \\cos(${fmt(angularFrequency)} \\times t + ${fmt(phase)})",
            result = ""
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("Циклическая частота", "ω", angularFrequency, "рад/с"),
                PhysicalQuantity("Начальная фаза", "φ", phase, "рад"),
                PhysicalQuantity("Продолжительность колебаний", "t", t, "с")
            )
        )
        return steps
    }
}