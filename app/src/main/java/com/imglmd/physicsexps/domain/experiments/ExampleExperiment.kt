package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class ExampleExperiment : Experiment {

    override val id = "pendulum"
    override val name = "Математический маятник"
    override val category = "Механика"
    override val description =
        "Математический маятник — это модель тела, подвешенного на невесомой нити, которое колеблется под действием силы тяжести."

    override val inputFields = listOf(
        InputField("length", "Длина нити", "L", "м", min = 0.0),
        InputField("period", "Период колебаний", "T", "с", min = 0.0, required = true),
        InputField("gravity", "Ускорение свободного падения", "g", "м/с²", min = 0.0)
    )

    override val minRequiredInputs = 2

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {

        val L = inputs["length"]
        val T = inputs["period"]
        val g = inputs["gravity"]

        val length: Double
        val period: Double
        val gravity: Double

        when {
            L != null && T != null -> {
                gravity = (4 * PI.pow(2) * L) / T.pow(2)
                length = L
                period = T
            }

            T != null && g != null -> {
                length = (g * T.pow(2)) / (4 * PI.pow(2))
                period = T
                gravity = g
            }

            L != null && g != null -> {
                period = 2 * PI * sqrt(L / g)
                length = L
                gravity = g
            }

            else -> {
                error("")
            }
        }

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Длина нити", "L", length, "м"),
                PhysicalQuantity("Период", "T", period, "с"),
                PhysicalQuantity("Ускорение", "g", gravity, "м/с²")
            )
        )
    }
}