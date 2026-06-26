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

class JouleLenzExperiment: Experiment {
    override val id = "joule_lenz"
    override val name = "joule_lenz"
    override val category = "electricity"
    override val description = "joule_lenz_desc"
    override val inputFields = listOf(
        InputField("time", "c_f_d", "t", "s", required = true, min = 0.0),
        InputField("amperage", "amperage", "I", "a", min = 0.0),
        InputField("voltage", "voltage", "U", "v", min = 0.0),
        InputField("resistance", "resistance", "R", "ohm", min = 0.0)
    )

    override val imageRes = R.drawable.joulelenz
    override val xLabel = "amperage"
    override val yLabel = "heat_am"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val t = inputs["time"]
        val I = inputs["amperage"]
        val U = inputs["voltage"]
        val R = inputs["resistance"]

        if (t == null) {
            return ValidationResult.Error(
                listOf(ValidationError.NotEnoughInputs)
            )
        }

        val count = listOf(I, U, R).count { it != null }

        if (count < 2) {
            return ValidationResult.Error(
                listOf(ValidationError.NotEnoughInputs)
            )
        }

        if (count > 2) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }

        if (R != null && R == 0.0) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }

        if (U != null && I != null && I == 0.0) {
            return ValidationResult.Error(
                listOf(ValidationError.InvalidCombination)
            )
        }

        return ValidationResult.Success(inputs)
    }

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
                PhysicalQuantity("c_f_d", "t", t, "s"),
                PhysicalQuantity("q_h", "Q", heat, "j"),
                PhysicalQuantity("amperage", "I", amperage, "a"),
                PhysicalQuantity("voltage", "U", voltage, "v"),
                PhysicalQuantity("resistance", "R", resistance, "ohm"),
                PhysicalQuantity("power", "P", power, "w"),
                PhysicalQuantity("electric_charge", "q", q, "c"),
                PhysicalQuantity("work_e", "A", work, "j")
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
            title = "solution_idea",
            body = "jou_step_1"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_2",
            expression = "I = \\frac{U}{R}"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_3",
            expression = "U = I R"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_4",
            expression = "R = \\frac{U}{I}"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_5",
            expression = "Q = I^2 R t"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_6",
            expression = "Q = U I t"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_7",
            expression = "Q = t \\frac{U^2}{R}"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_8",
            expression = "P = UI"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_9",
            expression = "A = P t"
        )

        steps += SolutionStep.Formula(
            description = "jou_step_10",
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
                description = "jou_step_11",
                expression = "Q = $t \\frac{$U^2}{$R}",
                result = "Q = ${fmt(heat)}"
            )
        }else if(R != null && I != null) {
            heat = I.pow(2)*R*t
            U = I * R
            steps += SolutionStep.Substitution(
                description = "jou_step_11",
                expression = "Q = $I^2 \\times $R \\times $t",
                result ="Q = ${fmt(heat)}"
            )
        }else if(U != null && I != null) {
            heat = I * U * t
            R = U / I
            steps += SolutionStep.Substitution(
                description = "jou_step_11",
                expression = "Q = $U \\times $I \\times $t",
                result ="Q = ${fmt(heat)}"
            )
        }

        power = U!! * I!!
        q = I * t
        work = power * t

        steps += SolutionStep.Substitution(
            description = "jou_step_8",
            expression = "P = ${fmt(U)} \\times ${fmt(I)}",
            result = "P = ${fmt(power)}"
        )

        steps += SolutionStep.Substitution(
            description = "jou_step_9",
            expression = "A = ${fmt(power)} \\times t",
            result = "A = ${fmt(work)}"
        )

        steps += SolutionStep.Substitution(
            description = "jou_step_10",
            expression = "q = ${fmt(I)} \\times $t",
            result = "q = ${fmt(q)}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("q_h", "Q", heat, "j"),
                PhysicalQuantity("amperage", "I", I, "a"),
                PhysicalQuantity("voltage", "U", U, "v"),
                PhysicalQuantity("resistance", "R", R!!, "ohm"),
                PhysicalQuantity("power", "P", power, "w"),
                PhysicalQuantity("electric_charge", "q", q, "c"),
                PhysicalQuantity("work_e", "A", work, "j")
            )
        )


        return steps
    }
}