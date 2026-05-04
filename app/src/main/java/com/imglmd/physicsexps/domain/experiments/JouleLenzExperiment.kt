package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.presentation.navigation.Screen
import kotlin.math.pow

class JouleLenzExperiment: Experiment {
    override val id = "joule_lenz"
    override val name = "Закон Джоуля-Ленца"
    override val category = "Электричество"
    override val description = "Закон Джоуля — Ленца — физический закон, определяющий количество теплоты, выделяемой проводником при прохождении электрического тока. Устанавливает связь между тепловым действием тока и электрическими параметрами цепи."
    override val inputFields = listOf(
        InputField("time", "Время прохождения тока", "t", "с", required = true, min = 0.0),
        InputField("amperage", "Сила тока", "I", "А", min = 0.0),
        InputField("voltage", "Напряжение", "U", "В", min = 0.0),
        InputField("resistance", "Сопротивление", "R", "Ом", min = 0.0)
    )

    override val minRequiredInputs = 3
    override val xLabel = "Сила тока"
    override val yLabel = "Количество теплоты, выделяемое проводником"

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val t = inputs["time"]
        val I = inputs["amperage"]
        val U = inputs["voltage"]
        val R = inputs["resistance"]

        val power: Double
        val heat: Double
        val amperage: Double
        val voltage: Double
        val resistance: Double
        val q: Double
        val work: Double
        val map = mutableMapOf<String, Double>()

        when {
            t != null && U != null && R != null -> {
                heat = (U.pow(2)/R) * t
                voltage = U
                resistance = R
                amperage = U / R
                power = U * amperage
                q = amperage * t
                work = power * t
                map.put("amperage", amperage)
                map.put("time", t)
                map.put("resistance", resistance)
            }

            t != null && R != null && I != null -> {
                heat = I.pow(2)*R*t
                voltage = I * R
                resistance = R
                amperage = I
                power = voltage * I
                q = I * t
                work = power * t
                map.put("amperage", amperage)
                map.put("time", t)
                map.put("resistance", resistance)
            }

            t != null && U != null && I != null -> {
                heat = I * U * t
                voltage = U
                amperage = I
                resistance = U / I
                power = U * I
                q = I * t
                work = power * t
                map.put("amperage", amperage)
                map.put("time", t)
                map.put("resistance", resistance)
            }

            else -> throw IllegalArgumentException("Нужно ввести три величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Время прохождения тока", "t", t, "с"),
                PhysicalQuantity("Количество теплоты", "Q", heat, "Дж"),
                PhysicalQuantity("Сила тока", "I", amperage, "А"),
                PhysicalQuantity("Напряжение", "U", voltage, "В"),
                PhysicalQuantity("Сопротивление", "R", resistance, "Ом"),
                PhysicalQuantity("Мощность", "P", power, "Вт"),
                PhysicalQuantity("Электрический заряд", "q", q, "Кл"),
                PhysicalQuantity("Работа электрического тока", "A", work, "Дж")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val amp = inputs.getValue("amperage")
        val time = inputs.getValue("time")
        val res = inputs.getValue("resistance")

        val startX = 0.0
        val step = amp / ExpConstants.DEFAULT_POINTS_COUNT
        var x = startX

        while(x <= amp + step) {
            val y = x.pow(2)*res*time
            list.add(Pair(x,y))
            x += step
        }
        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "Идея решения",
            body = "При прохождении электрического тока по проводнику выделяется теплота. " +
                    "Количество этой теплоты прямо пропорционально квадрату силы тока, сопротивлению " +
                    "проводника и времени его протекания."
        )

        steps += SolutionStep.Formula(
            description = "Найдём силу тока по закону Ома.",
            expression = "I = \\frac{U}{R}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём напряжение по закону Ома.",
            expression = "U = I R"
        )

        steps += SolutionStep.Formula(
            description = "Найдём сопротивление по закону Ома.",
            expression = "R = \\frac{U}{I}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём количество теплоты, используя закон Джоуля-Ленца. " +
                    "При известных силе тока и сопротивлении проводника.",
            expression = "Q = I^2 R t"
        )

        steps += SolutionStep.Formula(
            description = "Найдём количество теплоты, используя закон Джоуля-Ленца. При известных силе тока и напряжении.",
            expression = "Q = U I t"
        )

        steps += SolutionStep.Formula(
            description = "Найдём количество теплоты, используя закон Джоуля-Ленца. При известных напряжении и сопротивлении.",
            expression = "Q = t \\frac{U^2}{R}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём мощность электрического тока.",
            expression = "P = UI"
        )

        steps += SolutionStep.Formula(
            description = "Найдём работу электрического тока.",
            expression = "A = P t"
        )

        steps += SolutionStep.Formula(
            description = "Найдём величину электрического заряда.",
            expression = "q = I t"
        )

        if (inputs == null) return steps

        val fmt = {d: Double -> "%.2f".format(d)}

        val t = inputs.getValue("time")
        var I = inputs["amperage"]
        var U = inputs["voltage"]
        var R = inputs["resistance"]

        val power: Double
        var heat: Double = 0.0
        val q: Double
        val work: Double

        if (U != null && R != null) {
            heat = (U.pow(2)/R) * t
            I = U / R
            steps += SolutionStep.Substitution(
                description = "Найдём количество теплоты",
                expression = "Q = $t \\frac{$U^2}{$R}",
                result = "Q = ${fmt(heat)} \\text{Дж}"
            )
        }else if(R != null && I != null) {
            heat = I.pow(2)*R*t
            U = I * R
            steps += SolutionStep.Substitution(
                description = "Найдём количество теплоты",
                expression = "Q = $I^2 \\times $R \\times $t",
                result ="Q = ${fmt(heat)} \\text{Дж}"
            )
        }else if(U != null && I != null) {
            heat = I * U * t
            R = U / I
            steps += SolutionStep.Substitution(
                description = "Найдём количество теплоты",
                expression = "Q = $U \\times $I \\times $t",
                result ="Q = ${fmt(heat)} \\text{Дж}"
            )
        }

        power = U!! * I!!
        q = I * t
        work = power * t

        steps += SolutionStep.Substitution(
            description = "Найдём мощность электрического тока.",
            expression = "P = ${fmt(U)} \\times ${fmt(I)}",
            result = "P = ${fmt(power)} \\text{Вт}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём работу электрического тока.",
            expression = "A = ${fmt(power)} \\times t",
            result = "A = ${fmt(work)} \\text{Дж}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём величину электрического заряда.",
            expression = "q = ${fmt(I)} \\times $t",
            result = "q = ${fmt(q)} \\text{Кл}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("Количество теплоты", "Q", heat, "Дж"),
                PhysicalQuantity("Сила тока", "I", I, "А"),
                PhysicalQuantity("Напряжение", "U", U, "В"),
                PhysicalQuantity("Сопротивление", "R", R!!, "Ом"),
                PhysicalQuantity("Мощность", "P", power, "Вт"),
                PhysicalQuantity("Электрический заряд", "q", q, "Кл"),
                PhysicalQuantity("Работа электрического тока", "A", work, "Дж")
            )
        )


        return steps
    }
}