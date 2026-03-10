package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.PI
import kotlin.math.pow

class ExampleExperiment: Experiment {
    override val id = "pendulum"
    override val name = "Математический маятник"
    override val category = "Механика"
    override val description = "Математический маятник — это модель тела, подвешенного на невесомой нити, которое колеблется под действием силы тяжести."
    override val inputFields = listOf(
        InputField("length", "Длина нити", "L","метр"),
        InputField("period", "Период колебаний", "T", "сек")
    )


    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val L = inputs["length"]!!
        val T = inputs["period"]!!
        val g = (4 * PI.pow(2) * L) / T.pow(2)

        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity("Длина нити", "L",L, "м"),
                PhysicalQuantity("Период", "T",T, "с"),
                PhysicalQuantity("Ускорение", "g",g, "м/с²")
            ),
            "${LocalDate.now()}"
        )
    }
}