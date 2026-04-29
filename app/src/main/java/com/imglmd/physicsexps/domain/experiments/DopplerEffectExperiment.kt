package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.roundToInt
import kotlin.math.sin

class DopplerEffectExperiment: Experiment {
    override val id = "doppler_effect"
    override val name = "Эффект Доплера"
    override val category = "Акустика"
    override val description = "Эффект Доплера — это изменение частоты волны (звука, света, радиосигнала) из-за движения источника или наблюдателя."
    override val inputFields = listOf(
        InputField("v_obs", "Скорость наблюдателя", "vₒ", "м/с",
            required = true, max = ExpConstants.SPEED_OF_SOUND_IN_AIR),
        InputField("v_source", "Скорость источника", "vₛ", "м/с",
            required = true, max = ExpConstants.SPEED_OF_SOUND_IN_AIR),
        InputField("frequency", "Исходная частота", "fₛ", "Гц", required = true, min = 20.0, max = 20000.0),
    )
    override val minRequiredInputs = 3
    override val xLabel = "Скорость источника, м/с"
    override val yLabel = "Частота при удалении, Гц"

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val vO = inputs["v_obs"]
        val vS = inputs["v_source"]
        val fS = inputs["frequency"]
        val map = mutableMapOf<String, Double>()
        val v = ExpConstants.SPEED_OF_SOUND_IN_AIR

        val fSep: Double // отдаление
        val fApr: Double // сближение
        val shiftSep: Double
        val shiftApr: Double
        val lengthWaveSep: Double
        val lengthWaveApr: Double

        when {
            vO != null && vS != null && fS != null -> {
                fApr = fS * ((v + vO)/(v - vS))
                fSep = fS * ((v - vO)/(v + vS))
                shiftSep = fSep - fS
                shiftApr = fApr - fS
                lengthWaveApr = (v - vS) / fS
                lengthWaveSep = (v + vS) / fS

                map.put("v_source", vS)
                map.put("v_obs", vO)
                map.put("frequency", fS)
                map.put("v", v)
            }
            else -> throw IllegalArgumentException("Нужно ввести любые три величины")
        }

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("Частота звука при сближении", "f_apr", fApr, "Гц"),
                PhysicalQuantity("Частота звука при отдалении", "f_sep", fSep, "Гц"),
                PhysicalQuantity("Доплеровский сдвиг при сближении", "Δf_apr", shiftApr, "Гц"),
                PhysicalQuantity("Доплеровский сдвиг при отдалении", "Δf_sep", shiftSep, "Гц"),
                PhysicalQuantity("Длина волны при сближении", "λ_apr", lengthWaveApr, "м"),
                PhysicalQuantity("Длина волны при отдалении", "λ_sep", lengthWaveSep, "м")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel,
            date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
        )
    }

    override fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>> {
        val list = mutableListOf<Pair<Double, Double>>()
        val vS = inputs.getValue("v_source")
        val vO = inputs.getValue("v_obs")
        val fS = inputs.getValue("frequency")
        val v = inputs.getValue("v")

        val startX = 0.0
        val step = vS / ExpConstants.DEFAULT_POINTS_COUNT
        var x = startX
        while (x <= vS + step) {
            val y = fS * ((v - vO)/(v + x))
            list.add(Pair(x, y))
            x += step
        }

        return list
    }
}