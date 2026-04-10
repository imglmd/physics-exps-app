package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDate
import kotlin.math.pow

class FreeFallExperiment: Experiment {
    override val id = "free_fall"
    override val name = "Свободное падение тел"
    override val category = "Кинематика"
    override val description = "Свободным падением тел называют движение, которое совершается под действием только силы тяжести."
    override val inputFields = listOf(
        InputField("start_speed", "Начальная скорость", "v0", "м/c"),
        InputField("duration", "Продолжительность движения тела", "t", "с")
    )

    override val minRequiredInputs = 1

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val v0 = inputs["start_speed"] ?: 0.0
        val t: Double = inputs["duration"] ?: 0.0
        val g = 9.81
        val v: Double = v0 + g*t
        val h: Double = v0*t + (g * t.pow(2)) / 2
        return ExperimentResult(
            experiment = this,
            quantities = listOf(
                PhysicalQuantity(
                    label = "Скорость в момент времени, указанный пользователем",
                    symbol = "v", v, "м/с"
                ),
                PhysicalQuantity(
                    label = "Расстояние, которое проходит тело (высота)",
                    symbol = "h", h, "м"
                )
            ),
            "${LocalDate.now()}"
        )
    }
}