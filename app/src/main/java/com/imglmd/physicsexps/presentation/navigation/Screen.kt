package com.imglmd.physicsexps.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen
    @Serializable
    data class Experiment(
        val id: String,
        val inputs: Map<String, String>? = null,
        val replaceRunId: Int? = null
    ) : Screen
    @Serializable
    data class Result(val runId: Int? = null) : Screen
    @Serializable
    data object History : Screen
    @Serializable
    data class FullScreenChart(
        val runId: Int
    ) : Screen
    @Serializable
    data object Solution : Screen
}