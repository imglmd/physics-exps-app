package com.imglmd.physicsexps.domain.model

import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.InputField

interface Experiment {
    val id: String
    val name: String
    val category: String
    val description: String
    val imageRes: Int get() = R.drawable.placeholder  //TODO: брать картинку с сервера

    val inputFields: List<InputField>
    val minRequiredInputs: Int

    val xLabel: String
    val yLabel: String

    fun calculate(inputs: Map<String, Double>): ExperimentResult
    fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>>

}