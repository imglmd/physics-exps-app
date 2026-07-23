package com.imglmd.physicsexps.domain.model

import com.imglmd.physicsexps.domain.validation.ValidationResult

interface Experiment {
    val id: String
    val category: String
    val description: String

    val inputFields: List<InputField>
    val additionalInputFields: List<InputField> get() = emptyList()

    val xLabel: String
    val yLabel: String

    fun calculate(inputs: Map<String, Double>): ExperimentResult
    fun getPoints(inputs: Map<String, Double>): List<Pair<Double, Double>>

    /**
     * эксперимент сам решает,
     * подходит ли набор данных
     */
    fun validateInputs(inputs: Map<String, Double>): ValidationResult {
        return ValidationResult.Success(inputs)
    }

    /**
     * пустой список - режим обучения недоступен для этого эксперимента
     * inputs null - возвращает только теорию и формулы без подстановки
     */
    fun getSolutionSteps(inputs: Map<String, Double>? = null): List<SolutionStep> = emptyList()
}