package com.imglmd.physicsexps.domain.model

interface SolutionStep {

    data class Theory(
        val title: String,
        val body: String
    ): SolutionStep

    data class Formula(
        val description: String,
        val expression: String,   // "L = v₀*cos(α)*t"
    ): SolutionStep

    data class Substitution(
        val description: String,
        val expression: String,   // "L = 10.0 * cos(45°) * 1.44"
        val result: String        // "L = 10.19 м"
    ): SolutionStep

    data class Result(
        val quantity: PhysicalQuantity
    ): SolutionStep
}