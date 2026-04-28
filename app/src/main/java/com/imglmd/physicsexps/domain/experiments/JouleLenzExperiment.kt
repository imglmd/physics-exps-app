package com.imglmd.physicsexps.domain.experiments

import androidx.compose.ui.unit.Velocity
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDateTime
import java.time.ZoneOffset
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
                work = U * amperage * t
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
            yLabel = yLabel,
            date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

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
}