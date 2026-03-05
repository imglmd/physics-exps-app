package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import kotlin.math.PI
import kotlin.math.pow

class ExampleExperiment: Experiment {
    override val id = "pendulum"
    override val name = "Математический маятник"
    override val category = "Механика"
    override val description = "Математический маятник — это идеализированная физическая модель, " +
            "представляющая собой материальную точку, подвешенную на невесомой нерастяжимой нити и " +
            "способную совершать колебания в вертикальной плоскости под действием силы тяжести."
    override val inputFields = listOf(
        InputField("length", "Длина нити", "L","метр"),
        InputField("period", "Период колебаний", "T", "сек")
    )


    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val L = inputs["length"]!!
        val T = inputs["period"]!!
        val g = (4 * PI.pow(2) * L) / T.pow(2)

        return ExperimentResult(
            quantities = listOf(
                PhysicalQuantity("Длина нити", L, "м"),
                PhysicalQuantity("Период", T, "с"),
                PhysicalQuantity("g", g, "м/с²")
            )
        )
    }
}