package com.imglmd.physicsexps.domain.experiments

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.model.InputField
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import kotlin.math.abs

class DopplerEffectExperiment: Experiment {
    override val id = "doppler_effect"
    override val name = "doppler_effect"
    override val category = "phonics"
    override val description = "doppler_desc"
    override val inputFields = listOf(
        InputField("v_obs", "obs_v", "vₒ", "m_s",
            required = true, max = ExpConstants.SPEED_OF_SOUND_IN_AIR),
        InputField("v_source", "sor_v", "vₛ", "m_s",
            required = true, max = ExpConstants.SPEED_OF_SOUND_IN_AIR),
        InputField("frequency", "initial_frequency", "fₛ", "hz", required = true, min = 20.0, max = 20000.0),
    )
    override val xLabel = "sv"
    override val yLabel = "rm"
    override val imageRes = R.drawable.doppler

    override fun validateInputs(
        inputs: Map<String, Double>
    ): ValidationResult {
        val vObs = inputs["v_obs"]
        val vSource = inputs["v_source"]
        val frequency = inputs["frequency"]

        if (vObs == null || vSource == null || frequency == null) {
            return ValidationResult.Error(
                listOf(ValidationError.NotEnoughInputs)
            )
        }

        val soundSpeed = ExpConstants.SPEED_OF_SOUND_IN_AIR

        if (vSource >= soundSpeed || abs(vObs) >= soundSpeed) {
            return ValidationResult.Error(
                listOf(
                    ValidationError.InvalidCombination
                )
            )
        }

        return ValidationResult.Success(inputs)
    }

    override fun calculate(inputs: Map<String, Double>): ExperimentResult {
        val vO = inputs.getValue("v_obs")
        val vS = inputs.getValue("v_source")
        val fS = inputs.getValue("frequency")
        val map = mutableMapOf<String, Double>()
        val v = ExpConstants.SPEED_OF_SOUND_IN_AIR

        val fApr = fS * ((v + vO)/(v - vS))
        val fSep = fS * ((v - vO)/(v + vS))
        val shiftSep = fSep - fS
        val shiftApr = fApr - fS
        val lengthWaveApr = (v - vS) / fS
        val lengthWaveSep = (v + vS) / fS

        map["v_source"] = vS
        map["v_obs"] = vO
        map["frequency"] = fS
        map["v"] = v

        return ExperimentResult(
            experimentId = this.id,
            quantities = listOf(
                PhysicalQuantity("sor_f_apr", "f_apr", fApr, "hz"),
                PhysicalQuantity("s_f_a", "f_sep", fSep, "hz"),
                PhysicalQuantity("dop_sh_apr", "Δf_apr", shiftApr, "hz"),
                PhysicalQuantity("dop_sh_r", "Δf_sep", shiftSep, "hz"),
                PhysicalQuantity("w_l_apr", "λ_apr", lengthWaveApr, "m"),
                PhysicalQuantity("w_l_rec", "λ_sep", lengthWaveSep, "m")
            ),
            points = getPoints(map),
            xLabel = xLabel,
            yLabel = yLabel
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

    override fun getSolutionSteps(inputs: Map<String, Double>?): List<SolutionStep> {
        val steps = mutableListOf<SolutionStep>()

        steps += SolutionStep.Theory(
            title = "solution_idea",
            body = "dop_step_1"
        )

        steps += SolutionStep.Formula(
            description = "dop_step_2",
            expression = "f_apr = f_0 (\\frac{c + v_o}{c - v_s})"
        )

        steps += SolutionStep.Formula(
            description = "Найдём частоту звука при отдалении.",
            expression = "f_sep = f_0 (\\frac{c - v_o}{c + v_s})"
        )

        steps += SolutionStep.Formula(
            description = "Найдём доплеровский сдвиг при сближении - изменение частоты волны " +
                    "звука из-за движения источника или наблюдателя.",
            expression = "Δf_apr = f_apr - f_0"
        )

        steps += SolutionStep.Formula(
            description = "Найдём доплеровский сдвиг при отдалении - изменение частоты волны " +
                    "звука из-за движения источника или наблюдателя.",
            expression = "Δf_sep = f_sep - f_0"
        )

        steps += SolutionStep.Formula(
            description = "Найдём длину звуковой волны при сближении.",
            expression = "\\lambda_apr = \\frac{c - v_s}{f_0}"
        )

        steps += SolutionStep.Formula(
            description = "Найдём длину звуковой волны при отдалении.",
            expression = "\\lambda_sep = \\frac{c + v_s}{f_0}"
        )

        if (inputs == null) return steps

        val vO = inputs.getValue("v_obs")
        val vS = inputs.getValue("v_source")
        val f0 = inputs.getValue("frequency")
        val v = ExpConstants.SPEED_OF_SOUND_IN_AIR

        val fApr: Double = f0 * ((v + vO)/(v - vS)) // сближение
        val fSep: Double = f0 * ((v - vO)/(v + vS)) // отдаление
        val shiftSep = fSep - f0
        val shiftApr = fApr - f0
        val lengthWaveApr: Double = (v - vS) / f0
        val lengthWaveSep: Double = (v + vS) / f0

        val fmt = {d: Double -> "%.2f".format(d)}

        steps += SolutionStep.Substitution(
            description = "dop_step_2",
            expression = "f_apr = $f0 (\\frac{$v + $vO}{$v - $vS})",
            result = "f_apr = ${fmt(fApr)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём частоту звука при отдалении",
            expression = "f_sep = $f0 (\\frac{$v - $vO}{$v + $vS})",
            result = "f_sep = ${fmt(fSep)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём доплеровский сдвиг при сближении",
            expression = "Δf_apr = ${fmt(fApr)} - $f0",
            result = "Δf_apr = ${fmt(shiftApr)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём доплеровский сдвиг при отдалении",
            expression = "Δf_sep = ${fmt(fSep)} - $f0",
            result ="Δf_sep = ${fmt(shiftSep)} \\text{Гц}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём длину звуковой волны при сближении",
            expression = "\\lambda_apr = \\frac{$v - $vS}{$f0}",
            result = "\\lambda_apr = ${fmt(lengthWaveApr)} \\text{м}"
        )

        steps += SolutionStep.Substitution(
            description = "Найдём длину звуковой волны при отдалении",
            expression = "\\lambda_sep = \\frac{$v + $vS}{$f0}",
            result = "\\lambda_sep = ${fmt(lengthWaveSep)} \\text{м}"
        )

        steps += SolutionStep.Result(
            listOf(
                PhysicalQuantity("sor_f_apr", "f_apr", fApr, "hz"),
                PhysicalQuantity("s_f_a", "f_sep", fSep, "hz"),
                PhysicalQuantity("dop_sh_apr", "Δf_apr", shiftApr, "hz"),
                PhysicalQuantity("dop_sh_r", "Δf_sep", shiftSep, "hz"),
                PhysicalQuantity("w_l_apr", "λ_apr", lengthWaveApr, "m"),
                PhysicalQuantity("w_l_rec", "λ_sep", lengthWaveSep, "m")
            )
        )
        return steps
    }
}