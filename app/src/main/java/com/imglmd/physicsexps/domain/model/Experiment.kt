package com.imglmd.physicsexps.domain.model

import com.imglmd.physicsexps.domain.model.InputField

interface Experiment {
    val id: String
    val name: String
    val category: String
    val description: String
    val inputFields: List<InputField>
    val minRequiredInputs: Int
    fun calculate(inputs: Map<String, Double>): ExperimentResult
    fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>>
}