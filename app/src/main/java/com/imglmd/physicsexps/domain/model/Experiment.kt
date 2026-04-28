package com.imglmd.physicsexps.domain.model

import com.imglmd.physicsexps.R

interface Experiment {
    val id: String
    val name: String
    val category: String
    val description: String
    val imageRes: Int get() = R.drawable.placeholder  //TODO: брать картинку с сервера

    val inputFields: List<InputField>
    val minRequiredInputs: Int
    val additionalInputFields: List<InputField> get() = emptyList()

    val xLabel: String
    val yLabel: String

    fun calculate(inputs: Map<String, Double>): ExperimentResult
    fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>>

    /**
     * пустой список - режим обучения недоступен для этого эксперимента
     * inputs null - возвращает только теорию и формулы без подстановки
     */
    fun getSolutionSteps(inputs: Map<String, Double>? = null): List<SolutionStep> = emptyList()
}