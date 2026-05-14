package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import kotlin.math.pow

class FreeFallExperiment: Experiment {
    override val id = "free_fall"
    override val name = "Свободное падение тел"
    override val category = "Кинематика"
    override val description = "Свободным падением тел называют движение, которое совершается под действием только силы тяжести."
    override val inputFields = listOf(
        InputField("start_speed", "Начальная скорость", "v₀", "м/c", required = false),
        InputField("duration", "Продолжительность движения тела", "t", "с", required = true, min = 0.0)
    )
    override val imageRes = R.drawable.freefall
    override val xLabel =  "Время, с"
    override val yLabel = "у"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val time = inputs["duration"] ?: return ValidationResult.Error(
            listOf(ValidationError.NotEnoughInputs)
        )

        if (time == 0.0) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }

        return ValidationResult.Success(inputs)
    }

    override fun calculate(
        inputs: Map<String, Double>
    ): ExperimentResult {

        val v0 = inputs["start_speed"] ?: 0.0
        val t = inputs.getValue("duration")

        val g = ExpConstants.GRAVITY

        val v = v0 + g * t
        val h = v0 * t + (g * t.pow(2)) / 2

        val map = mapOf(
            "start_speed" to v0,
            "duration" to t
        )

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Начальная скорость", "v₀",v0, "м/с"),
                PhysicalQuantity("Продолжительность движения тела", "t", t, "с"),
                PhysicalQuantity(
                    label = "Скорость в момент времени",
                    symbol = "v",
                    value = v,
                    unit = "м/с"
                ),
                PhysicalQuantity(
                    label = "Пройденное расстояние",
                    symbol = "h",
                    value = h,
                    unit = "м"
                )
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val v0 = inputs.getValue("start_speed")
        val t = inputs.getValue("duration")
        val g = ExpConstants.GRAVITY

        val startX = 0.0
        val step = t / ExpConstants.DEFAULT_POINTS_COUNT

        var x = startX
        while(x <= t + step) {
            val y = v0 * x + ((g*x.pow(2))/2)
            list.add(Pair(x, y))
            x += step
        }

        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "Идея решения",
            body = "Использовании уравнений равноускоренного движения, где ускорение всегда постоянно и равно ускорению свободного падения g."
        )

        steps += SolutionStep.Formula(
            description = "Найдём скорость в указанный момент времени.",
            expression = "v = v_0 + gt"
        )

        steps += SolutionStep.Formula(
            description = "Найдём расстояние, которое проходит тело по вертикали.",
            expression = "h = v_0t + \\frac{g t^2}{2}"
        )

        if (inputs == null) return steps

        val v0 = inputs.getValue("start_speed")
        val t = inputs.getValue("duration")
        val g = ExpConstants.GRAVITY
        val v: Double
        val h: Double

        v = v0 + g*t
        h = v0*t + (g * t.pow(2)) / 2

        val fmt = {d: Double -> "%.2f".format(d)}

        steps += SolutionStep.Substitution(
            description = "Найдём скорость в указанный момент времени.",
            expression = "v = $v0 + $g \\times $t",
            result = "v = ${fmt(v)} \\text{м/c}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём расстояние, которое проходит тело по вертикали.",
            expression = "h = $v0 \\times $t + \\frac{$g \\times $t^2}{2}",
            result = "h = ${fmt(h)} \\text{м}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity(
                    label = "Скорость в момент времени, указанный пользователем",
                    symbol = "v", v, "м/с"
                ),
                PhysicalQuantity(
                    label = "Расстояние, которое проходит тело",
                    symbol = "h", h, "м"
                )
            )
        )
        return steps
    }
}