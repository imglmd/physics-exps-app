package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs
import kotlin.math.pow

class CoulombsLawExperiment: Experiment {
    override val id = "coulombs_law"
    override val name = "Закон Кулона"
    override val category = "Электричество"
    override val description = "Закон Кулона — это физический закон, описывающий силу, с которой электрически заряженные тела притягиваются или отталкиваются. Чем больше заряды, тем сильнее взаимодействие."
    override val imageRes = R.drawable.coulombs_law
    override val inputFields = listOf(
        InputField("q1", "Первый заряд × 10⁻⁹", "q1", "Кл"),
        InputField("q2", "Второй заряд × 10⁻⁹", "q2", "Кл"),
        InputField("distance", "Расстояние между зарядами", "r", "м")
    )
    override val xLabel = "Расстояние, м"
    override val yLabel = "Электрическая сила, нН"

    override val minRequiredInputs = 3

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val q1 = inputs["q1"]
        val q2 = inputs["q2"]
        val r = inputs["distance"]
        val k = ExpConstants.COULOMB_CONSTANT
        val nano = ExpConstants.NANO
        val nanoInv = ExpConstants.NANO_INVERSE

        val f: Double
        val intensity1: Double
        val intensity2: Double
        val potentialEnergy: Double
        val map = mutableMapOf<String, Double>()

        when {
            q1 != null && q2 != null && r != null -> {
                f = k * (abs(q1 * q2 * nano * nano) /(r*r)) * nanoInv
                intensity1 = k * q1 * ExpConstants.NANO / r.pow(2)
                intensity2 = k * q2 * ExpConstants.NANO / r.pow(2)
                potentialEnergy = k * (q1*q2*ExpConstants.NANO*ExpConstants.NANO/r) * ExpConstants.NANO_INVERSE
                map.put("q1", q1)
                map.put("q2", q2)
                map.put("distance", r)
            }

            else -> throw IllegalArgumentException("Нужно ввести любые три величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Первый заряд", "q1", q1, "нКл"),
                PhysicalQuantity("Второй заряд", "q2", q2, "нКл"),
                PhysicalQuantity("Расстояние", "r", r, "м"),
                PhysicalQuantity("Электрическая сила", "F", f, "нН"),
                PhysicalQuantity("Напряжённость 1-го заряда", "E1", intensity1, "В/м"),
                PhysicalQuantity("Напряжённость 2-го заряда", "E2", intensity2, "В/м"),
                PhysicalQuantity("Потенциальная энергия взаимодействия", "W",
                    potentialEnergy, "нДж")
            ),
            points = getPoints(map),
            date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val k = ExpConstants.COULOMB_CONSTANT
        val q1: Double = inputs.getValue("q1") * ExpConstants.NANO
        val q2: Double = inputs.getValue("q2") * ExpConstants.NANO
        val r: Double = inputs.getValue("distance")

        val startX = r * 0.1
        val step = (r - startX) / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while(x <= r + step) {
            val y = (k * (abs(q1 * q2) /(x*x))) * ExpConstants.NANO_INVERSE
            list.add(Pair(x, y))
            x += step
        }

        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "Идея рещения",
            body = "Два неподвижных точечных электрических заряда в вакууме взаимодействуют " +
                    "с силой, прямо пропорциональной произведению модулей этих " +
                    "зарядов и обратно пропорциональной квадрату расстояния между ними."
        )

        steps += SolutionStep.Formula(
            description = "Найдём электрическую силу, используя закон Кулона.",
            expression = "F = k \\frac{|q_1||q_2|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём напряжённость 1-го заряда - векторная физическая величина, характеризующая силу воздействия поля на заряженные частицы в данной точке пространства.",
            expression = "E_1 = k \\frac{|q_1|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём напряжённость 2-го заряда - векторная физическая величина, характеризующая силу воздействия поля на заряженные частицы в данной точке пространства.",
            expression = "E_2 = k \\frac{|q_2|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём потенциальную энергию взаимодействия зарядов.",
            expression = "W = k \\frac{q_1 q_2}{r}"
        )

        if (inputs == null) return steps

        val q1 = inputs.getValue("q1")
        val q2 = inputs.getValue("q2")
        val r = inputs.getValue("distance")
        val k = ExpConstants.COULOMB_CONSTANT

        val F = k*(q1*q2*ExpConstants.NANO*ExpConstants.NANO/r) * ExpConstants.NANO_INVERSE
        val E1 = k * q1 * ExpConstants.NANO / r.pow(2)
        val E2 = k * q2 * ExpConstants.NANO / r.pow(2)
        val W = k * (q1*q2*ExpConstants.NANO*ExpConstants.NANO/r) * ExpConstants.NANO_INVERSE

        val fmt = { d: Double -> "%.2f".format(d) }
        val fmt2 = { d: Double -> "%.4f".format(d) }

        steps += SolutionStep.Substitution(
            description = "Найдём электрическую силу",
            expression = "F = 9 \\times 10^9 \\frac{|${fmt(q1)}||${fmt(q2)}|}{$r^2}",
            result = "F = ${fmt(F)} \\text{нН}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём напряжённость 1-го заряда",
            expression = "E_1 = 9 \\times 10^9 \\frac{|${fmt(q1)}|{$r^2}",
            result = "E_1 = ${fmt2(E1)} \\text{В/м}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём напряжённость 2-го заряда",
            expression = "E_2 = 9 \\times 10^9 \\frac{|${fmt(q2)}|}{r^2}",
            result = "E2 = ${fmt2(E2)} \\text{В/м}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём потенциальную энергию взаимодействия зарядов",
            expression = "W = 9 \\times 10^9 \\frac{${fmt(q1)} ${fmt(q2)}}{r}",
            result ="W = ${fmt(W)} \\text{нДж}"
        )

        steps += SolutionStep.Result(
            quantities = listOf(
                PhysicalQuantity("Электрическая сила", "F", F, "нН"),
                PhysicalQuantity("Напряжённость 1-го заряда", "E1", E1, "В/м"),
                PhysicalQuantity("Напряжённость 2-го заряда", "E2", E2, "В/м"),
                PhysicalQuantity("Потенциальная энергия взаимодействия", "W",
                    W, "нДж")
            )
        )
        return steps
    }
}