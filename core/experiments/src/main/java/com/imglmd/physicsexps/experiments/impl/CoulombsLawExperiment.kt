package com.imglmd.physicsexps.experiments.impl

import com.imglmd.physicsexps.experiments.ExpConstants
import com.imglmd.physicsexps.experiments.model.Experiment
import com.imglmd.physicsexps.experiments.model.ExperimentResult
import com.imglmd.physicsexps.experiments.model.InputField
import com.imglmd.physicsexps.experiments.model.PhysicalQuantity
import com.imglmd.physicsexps.experiments.model.SolutionStep
import com.imglmd.physicsexps.experiments.validation.ValidationError
import com.imglmd.physicsexps.experiments.validation.ValidationResult
import kotlin.collections.plusAssign
import kotlin.math.abs
import kotlin.math.pow

class CoulombsLawExperiment : Experiment {
    override val id = "coulombs_law"
    override val category = "electricity"
    override val description = "coulombs_law_desc"
    override val inputFields = listOf(
        InputField("q1", "q1_10_9", "q1", "Кл"),
        InputField("q2", "q2_10_9", "q2", "Кл"),
        InputField("distance", "col_dist", "r", "м", min = 1e-9)
    )
    override val xLabel = "dist"
    override val yLabel = "nano"

    override fun validateInputs(inputs: Map<String, Double>): ValidationResult {
        val q1 = inputs["q1"]
        val q2 = inputs["q2"]
        val distance = inputs["distance"]

        if (q1 == null || q2 == null || distance == null) {
            return ValidationResult.Error(
                listOf(ValidationError.NotEnoughInputs)
            )
        }
        return ValidationResult.Success(inputs)
    }

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
                f = k * (abs(q1 * q2 * nano * nano) / (r * r)) * nanoInv
                intensity1 = k * q1 * ExpConstants.NANO / r.pow(2)
                intensity2 = k * q2 * ExpConstants.NANO / r.pow(2)
                potentialEnergy =
                    k * (q1 * q2 * ExpConstants.NANO * ExpConstants.NANO / r) * ExpConstants.NANO_INVERSE
                map.put("q1", q1)
                map.put("q2", q2)
                map.put("distance", r)
            }

            else -> error("")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("first_charge", "q1", q1, "nC"),
                PhysicalQuantity("second_charge", "q2", q2, "nC"),
                PhysicalQuantity("distance", "r", r, "m"),
                PhysicalQuantity("electric_force", "F", f, "nN"),
                PhysicalQuantity("field_strength_1", "E1", intensity1, "v_m"),
                PhysicalQuantity("field_strength_2", "E2", intensity2, "v_m"),
                PhysicalQuantity(
                    "potential_energy", "W",
                    potentialEnergy, "nJ"
                )
            ),
            points = getPoints(map),
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
        while (x <= r + step) {
            val y = (k * (abs(q1 * q2) / (x * x))) * ExpConstants.NANO_INVERSE
            list.add(Pair(x, y))
            x += step
        }

        return list
    }

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "sol_col_desc"
        )

        steps += SolutionStep.Formula(
            description = "col_step_1",
            expression = "F = k \\frac{|q_1||q_2|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "col_step_2",
            expression = "E_1 = k \\frac{|q_1|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "col_step_3",
            expression = "E_2 = k \\frac{|q_2|}{r^2}"
        )

        steps += SolutionStep.Formula(
            description = "col_step_4",
            expression = "W = k \\frac{q_1 q_2}{r}"
        )

        if (inputs == null) return steps

        val q1 = inputs.getValue("q1")
        val q2 = inputs.getValue("q2")
        val r = inputs.getValue("distance")
        val k = ExpConstants.COULOMB_CONSTANT

        val F =
            k * (q1 * q2 * ExpConstants.NANO * ExpConstants.NANO / r) * ExpConstants.NANO_INVERSE
        val E1 = k * q1 * ExpConstants.NANO / r.pow(2)
        val E2 = k * q2 * ExpConstants.NANO / r.pow(2)
        val W =
            k * (q1 * q2 * ExpConstants.NANO * ExpConstants.NANO / r) * ExpConstants.NANO_INVERSE

        val fmt = { d: Double -> "%.2f".format(d) }
        val fmt2 = { d: Double -> "%.4f".format(d) }

        steps += SolutionStep.Substitution(
            description = "col_step_5",
            expression = "F = 9 \\times 10^9 \\frac{|${fmt(q1)}||${fmt(q2)}|}{$r^2}",
            result = "F = ${fmt(F)}"
        )

        steps += SolutionStep.Substitution(
            description = "col_step_6",
            expression = "E_1 = 9 \\times 10^9 \\frac{|${fmt(q1)}|}{$r^2}",
            result = "E_1 = ${fmt2(E1)}"
        )

        steps += SolutionStep.Substitution(
            description = "col_step_7",
            expression = "E_2 = 9 \\times 10^9 \\frac{|${fmt(q2)}|}{$r^2}",
            result = "E2 = ${fmt2(E2)}"
        )

        steps += SolutionStep.Substitution(
            description = "col_step_4",
            expression = "W = 9 \\times 10^9 \\frac{${fmt(q1)} ${fmt(q2)}}{$r}",
            result = "W = ${fmt(W)}"
        )

        steps += SolutionStep.Result(
            quantities = listOf(
                PhysicalQuantity("electric_force", "F", F, "nN"),
                PhysicalQuantity("field_strength_1", "E1", E1, "v_m"),
                PhysicalQuantity("field_strength_2", "E2", E2, "v_m"),
                PhysicalQuantity(
                    "potential_energy", "W",
                    W, "nJ"
                )
            )
        )
        return steps
    }
}