package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import java.lang.Math.sin
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sqrt

class PhysicalPendulum: Experiment {
    override val id = "physical_pendulum"
    override val name = "Физический маятник"
    override val category = "Механика"
    override val description = "Осциллятор, представляющий собой твёрдое тело, совершающее " +
            "колебания в поле каких-либо сил относительно точки, не являющейся центром " +
            "масс этого тела, или неподвижной оси, перпендикулярной направлению действия сил " +
            "и не проходящей через центр масс этого тела."
    override val inputFields = listOf(
        InputField("moment", "Момент инерции тела относительно оси вращения",
            "I", "кг×м²", required = true),
        InputField("weight", "Масса тела", "m", "кг", required = true, min = 0.0),
        InputField("distance", "Расстояние от оси вращения до центра масс", "d",
            "м", required = true, min = 0.0)
    )

    override val additionalInputFields = listOf(
        InputField("angle", "Угол отклонения", "α", "°", min = 0.0,
            required = false)
    )

    override val minRequiredInputs = 4
    override val xLabel = "Расстояние от оси вращения до центра масс, м"
    override val yLabel = "Период колебаний, с"

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val i = inputs.getValue("moment")
        val m = inputs.getValue("weight")
        val d = inputs.getValue("distance")
        val a = inputs["angle"]
        val g = ExpConstants.GRAVITY
        val map = mutableMapOf<String, Double>()

        val b: Double?
        val M: Double?
        val ep: Double?
        val w: Double?
        val period = 2 * PI * sqrt(i/m*g*d)
        val frequency = 1 / period
        val angularFrequency = sqrt(m*g*d/i)
        val _l = i / m * d


        if (a != null) {
            val rad = Math.toRadians(a)
            b = m*g*d*sin(rad)/i
            M = -m*g*d*sin(rad)
            ep = m*g*d*(1 - cos(rad))
            w = sqrt((2*m*g*d*(1-cos(rad)))/i)
        } else{
            b = null
            M =null
            ep = null
            w = null
        }

        map.put("weight", m)
        map.put("distance", d)
        map.put("moment", i)

        return ExperimentResult(
            experimentId = this.id,
            quantities = buildList {
                add(PhysicalQuantity("Момент инерции тела относительно оси вращения",
                    "I", i, "кг×м²"))
                add(PhysicalQuantity("Масса тела", "m", m, "кг"))
                add(PhysicalQuantity("Расстояние от оси вращения до центра масс", "d", d, "м"))
                add(PhysicalQuantity("Период колебаний", "T", period, "с"))
                add(PhysicalQuantity("Линейная частота", "ν", frequency, "Гц"))
                add(PhysicalQuantity("Циклическая частота", "ω₀", angularFrequency, "рад/с"))
                add(PhysicalQuantity("Длина математического маятника с таким же периодом",
                    "L", _l, "м"))
                if (a != null) {
                    add(PhysicalQuantity("Угол отклонения", "α", a, "°"))
                }
                if (b != null) {
                    add(PhysicalQuantity("Угловое ускорение", "β", b, "м/c²"))
                }
                if (M != null) {
                    add(PhysicalQuantity("Возвращающий момент сил", "M", M, "Н×м"))
                }
                if (ep != null) {
                    add(PhysicalQuantity("Потенциальная энергия", "E", ep, "Дж"))
                }
                if (w != null) {
                    add(PhysicalQuantity("Максимальная угловая скорость", "ω_max", w, "рад/с"))
                }
            },
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )

    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val dist = inputs.getValue("distance")
        val weight = inputs.getValue("weight")
        val moment = inputs.getValue("moment")
        val g = ExpConstants.GRAVITY

        val startX = 0.0
        val step = dist / ExpConstants.DEFAULT_POINTS_COUNT
        var x = startX
        while (x <= dist + step) {
            val y = 2 * PI * sqrt(moment/weight*g*x)
            list.add(Pair(x, y))
            x+=step
        }
        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "Идея решешения",
            body = "Физический маятник учитывает реальное характеристики тела: форму, размеры, " +
                    "массу и распределение этой массы относительно оси вращения."
        )

        steps += SolutionStep.Formula(
            description = "Найдём период колебаний физического маятника.",
            expression = "T = 2\\pi\\sqrt{\\frac{I}{m g d}}"
        )

        steps += SolutionStep.Formula(
            description = "Зная период, вычислим линейную частоту колебаний.",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "Вычислим циклическую частоту - число колебаний за 2π секунд.",
            expression = "\\omega = \\sqrt{\\frac{m g d}{I}}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём длину математического маятника с таким же периодом.",
            expression = "L = \\frac{I}{md}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём угловое ускорение - ускорение в момент времени, когда маятник отклонен на угол α.",
            expression = "\\beta = \\frac{m g d \\sin(\\alpha)}{I}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём возвращающий момент сил - момент силы тяжести, стремящийся вернуть маятник в равновесие.",
            expression = "M = -mgd\\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "Найдём потенциальную энергию - энергия маятника в точке отклонения на угол α.",
            expression = "E = mgd(1 - \\cos(\\alpha))"
        )

        steps += SolutionStep.Formula(
            description = "Найдём максимальную угловую скорость, достигается в момент прохождения положения равновесия.",
            expression = "\\omega_max = \\sqrt{\\frac{2 m g d (1 - \\cos(\\alpha))}{I}}"
        )

        if (inputs == null) return steps

        val i = inputs.getValue("moment")
        val m = inputs.getValue("weight")
        val d = inputs.getValue("distance")
        val a = inputs["angle"]
        val g = ExpConstants.GRAVITY

        val b: Double
        val M: Double
        val ep: Double
        val w: Double
        val period = 2 * PI * sqrt(i/m*g*d)
        val frequency = 1 / period
        val angularFrequency = sqrt(m*g*d/i)
        val _l = i / m * d

        val fmt = { d: Double -> "%.2f".format(d) }

        steps += SolutionStep.Substitution(
            description = "Найдём период колебаний физического маятника",
            expression = "T = 2\\pi\\sqrt{\\frac{$i}{$m \\times $g \\times $d}}",
            result = "T = ${fmt(period)} \\text{с}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём линейную частоту колебаний",
            expression = "\\nu = \\frac{1}{${fmt(period)}}",
            result = "\\nu = ${fmt(frequency)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём циклическую частоту",
            expression = "\\omega = \\sqrt{\\frac{$m \\times $g \\times $d}{$i}}",
            result = "\\omega = ${fmt(angularFrequency)}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём длину математического маятника с таким же периодом",
            expression = "L = \\frac{$i}{$m \\times $d}",
            result = "L = ${fmt(_l)} \\text{м}"
        )

        if (a != null) {
            val rad = Math.toRadians(a)
            b = m*g*d*sin(rad)/i
            M = -m*g*d*sin(rad)
            ep = m*g*d*(1 - cos(rad))
            w = sqrt((2*m*g*d*(1-cos(rad)))/i)

            steps += SolutionStep.Substitution(
                description = "Найдём угловое ускорение",
                expression = "\\beta = \\frac{$m \\times $g \\times $d\\sin($a)}{$i}",
                result = "\\beta = ${fmt(b)} \\text{м/c²}"
            )

            steps += SolutionStep.Substitution(
                description = "Найдём возвращающий момент сил",
                expression = "M = -$m \\times $g \\times $d\\sin($a)",
                result = "M = ${fmt(M)} \\text{Н×м}"
            )

            steps += SolutionStep.Substitution(
                description = "Найдём потенциальную энергию",
                expression = "E = $m \\times $g \\times $d(1 - \\cos($a))",
                result = "E = ${fmt(ep)} \\text{Дж}"
            )

            steps += SolutionStep.Substitution(
                description = "Найдём максимальную угловую скорость",
                expression = "\\omega_max = \\sqrt{\\frac{2$m \\times $g \\times $d(1 - \\cos($a))}{$i}}",
                result = "\\omega_max = ${fmt(w)} \\text{рад/с}"
            )
            steps += SolutionStep.Result(
                listOf(
                    PhysicalQuantity("Момент инерции тела относительно оси вращения",
                        "I", i, "кг×м²"),
                    PhysicalQuantity("Масса тела", "m", m, "кг"),
                    PhysicalQuantity("Расстояние от оси вращения до центра масс", "d", d, "м"),
                    PhysicalQuantity("Период колебаний", "T", period, "с"),
                    PhysicalQuantity("Линейная частота", "ν", frequency, "Гц"),
                    PhysicalQuantity("Циклическая частота", "ω₀", angularFrequency, "рад/с"),
                    PhysicalQuantity("Длина математического маятника с таким же периодом",
                        "L", _l, "м"),
                    PhysicalQuantity("Угол отклонения", "α", a, "°"),
                    PhysicalQuantity("Угловое ускорение", "β", b, "м/c²"),
                    PhysicalQuantity("Возвращающий момент сил", "M", M, "Н×м"),
                    PhysicalQuantity("Потенциальная энергия", "E", ep, "Дж"),
                    PhysicalQuantity("Максимальная угловая скорость", "ω_max", w, "рад/с")
                )
            )
        } else {
            steps += SolutionStep.Result(
                listOf(
                    (PhysicalQuantity("Момент инерции тела относительно оси вращения",
                        "I", i, "кг×м²")),
                    (PhysicalQuantity("Масса тела", "m", m, "кг")),
                    (PhysicalQuantity("Расстояние от оси вращения до центра масс", "d", d, "м")),
                    (PhysicalQuantity("Период колебаний", "T", period, "с")),
                    (PhysicalQuantity("Линейная частота", "ν", frequency, "Гц")),
                    (PhysicalQuantity("Циклическая частота", "ω₀", angularFrequency, "рад/с")),
                    (PhysicalQuantity("Длина математического маятника с таким же периодом",
                        "L", _l, "м"))
                )
            )
        }

        return steps
    }
}