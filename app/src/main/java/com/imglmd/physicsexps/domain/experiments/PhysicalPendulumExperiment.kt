package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import java.lang.Math.sin
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sqrt

class PhysicalPendulumExperiment: Experiment {
    override val id = "physical_pendulum"
    override val category = "mechanics"
    override val description = "physical_pendulum_desc"
    override val inputFields = listOf(
        InputField("moment", "moment", "I", "kg_m_2", required = true),
        InputField("weight", "weight", "m", "kg", required = true, min = 0.0),
        InputField("distance", "dist_ax", "d",
            "m", required = true, min = 0.0)
    )

    override val additionalInputFields = listOf(
        InputField("angle", "def_ang", "α", "ang", min = 0.0,
            required = false)
    )

    override val xLabel = "dist_axs"
    override val yLabel = "period_s"

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {

        val moment = inputs["moment"]
        val weight = inputs["weight"]
        val distance = inputs["distance"]
        val angle = inputs["angle"]

        val errors = mutableListOf<ValidationError>()

        if (moment == null) errors += ValidationError.RequiredField("moment")
        if (weight == null) errors += ValidationError.RequiredField("weight")
        if (distance == null) errors += ValidationError.RequiredField("distance")

        if (moment != null && moment <= 0.0) errors += ValidationError.OutOfRange("moment", min = 0.01)
        if (weight != null && weight <= 0.0) errors += ValidationError.OutOfRange("weight",min = 0.01)
        if (distance != null && distance <= 0.0) { errors += ValidationError.OutOfRange("distance", min = 0.01) }

        if (angle != null && (angle !in 0.0..90.0)) { errors += ValidationError.OutOfRange("angle", min = 0.0, max = 90.0) }

        return if (errors.isEmpty()) {
            ValidationResult.Success(inputs)
        } else {
            ValidationResult.Error(errors)
        }
    }

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
                add(PhysicalQuantity("moment", "I", i, "kg_m_2"))
                add(PhysicalQuantity("weight", "m", m, "kg"))
                add(PhysicalQuantity("dist_ax", "d", d, "m"))
                add(PhysicalQuantity("period_o", "T", period, "s"))
                add(PhysicalQuantity("linear_frequency", "ν", frequency, "hz"))
                add(PhysicalQuantity("ang_f", "ω₀", angularFrequency, "rad_s"))
                add(PhysicalQuantity("length_simp",
                    "L", _l, "m"))
                if (a != null) {
                    add(PhysicalQuantity("def_ang", "α", a, "°"))
                }
                if (b != null) {
                    add(PhysicalQuantity("ang_a", "β", b, "m_s_2"))
                }
                if (M != null) {
                    add(PhysicalQuantity("restoring_moment", "M", M, "n_m"))
                }
                if (ep != null) {
                    add(PhysicalQuantity("potential_energy_", "E", ep, "j"))
                }
                if (w != null) {
                    add(PhysicalQuantity("max_ang_v", "ω_max", w, "rad_s"))
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
            title = "solution_idea",
            body = "ph_step_1"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_2",
            expression = "T = 2\\pi\\sqrt{\\frac{I}{m g d}}"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_3",
            expression = "\\nu = \\frac{1}{T}"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_4",
            expression = "\\omega = \\sqrt{\\frac{m g d}{I}}"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_5",
            expression = "L = \\frac{I}{md}"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_6",
            expression = "\\beta = \\frac{m g d \\sin(\\alpha)}{I}"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_7",
            expression = "M = -mgd\\sin(\\alpha)"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_8",
            expression = "E = mgd(1 - \\cos(\\alpha))"
        )

        steps += SolutionStep.Formula(
            description = "ph_step_9",
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
            description = "ph_step_2",
            expression = "T = 2\\pi\\sqrt{\\frac{$i}{$m \\times $g \\times $d}}",
            result = "T = ${fmt(period)}"
        )

        steps += SolutionStep.Substitution(
            description = "ph_step_10",
            expression = "\\nu = \\frac{1}{${fmt(period)}}",
            result = "\\nu = ${fmt(frequency)}"
        )

        steps += SolutionStep.Substitution(
            description = "ph_step_11",
            expression = "\\omega = \\sqrt{\\frac{$m \\times $g \\times $d}{$i}}",
            result = "\\omega = ${fmt(angularFrequency)}"
        )

        steps += SolutionStep.Substitution(
            description = "ph_step_12",
            expression = "L = \\frac{$i}{$m \\times $d}",
            result = "L = ${fmt(_l)}"
        )

        if (a != null) {
            val rad = Math.toRadians(a)
            b = m*g*d*sin(rad)/i
            M = -m*g*d*sin(rad)
            ep = m*g*d*(1 - cos(rad))
            w = sqrt((2*m*g*d*(1-cos(rad)))/i)

            steps += SolutionStep.Substitution(
                description = "ph_step_13",
                expression = "\\beta = \\frac{$m \\times $g \\times $d\\sin($a)}{$i}",
                result = "\\beta = ${fmt(b)}"
            )

            steps += SolutionStep.Substitution(
                description = "ph_step_14",
                expression = "M = -$m \\times $g \\times $d\\sin($a)",
                result = "M = ${fmt(M)}"
            )

            steps += SolutionStep.Substitution(
                description = "ph_step_15",
                expression = "E = $m \\times $g \\times $d(1 - \\cos($a))",
                result = "E = ${fmt(ep)}"
            )

            steps += SolutionStep.Substitution(
                description = "ph_step_16",
                expression = "\\omega_max = \\sqrt{\\frac{2$m \\times $g \\times $d(1 - \\cos($a))}{$i}}",
                result = "\\omega_max = ${fmt(w)}"
            )
            steps += SolutionStep.Result(
                listOf(
                    PhysicalQuantity("moment", "I", i, "kg_m_2"),
                    PhysicalQuantity("weight", "m", m, "kg"),
                    PhysicalQuantity("dist_ax", "d", d, "m"),
                    PhysicalQuantity("period_o", "T", period, "s"),
                    PhysicalQuantity("linear_frequency", "ν", frequency, "hz"),
                    PhysicalQuantity("ang_f", "ω₀", angularFrequency, "rad_s"),
                    PhysicalQuantity("length_simp", "L", _l, "m"),
                    PhysicalQuantity("def_ang", "α", a, "°"),
                    PhysicalQuantity("ang_a", "β", b, "m_s_2"),
                    PhysicalQuantity("restoring_moment", "M", M, "n_m"),
                    PhysicalQuantity("potential_energy_", "E", ep, "j"),
                    PhysicalQuantity("max_ang_v", "ω_max", w, "rad_s")
                )
            )
        } else {
            steps += SolutionStep.Result(
                listOf(
                    (PhysicalQuantity("moment",
                        "I", i, "kg_m_2")),
                    (PhysicalQuantity("weight", "m", m, "kg")),
                    (PhysicalQuantity("dist_ax", "d", d, "m")),
                    (PhysicalQuantity("period_o", "T", period, "s")),
                    (PhysicalQuantity("linear_frequency", "ν", frequency, "hz")),
                    (PhysicalQuantity("ang_f", "ω₀", angularFrequency, "rad_s")),
                    (PhysicalQuantity("length_simp","L", _l, "m"))
                )
            )
        }

        return steps
    }
}