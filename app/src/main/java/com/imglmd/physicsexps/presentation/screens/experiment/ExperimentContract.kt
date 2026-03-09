package com.imglmd.physicsexps.presentation.screens.experiment

import com.imglmd.physicsexps.domain.model.Experiment

data class ExperimentState (
    val experiment: Experiment,
    val inputs: Map<String, String> = emptyMap(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val isButtonActive: Boolean = false
)
sealed interface ExperimentIntent {
    data object Start: ExperimentIntent
    data class ChangeValue(val key: String, val newValue: String): ExperimentIntent
}
sealed interface ExperimentAction {
    data object NavigateToResult: ExperimentAction
    data object NavigateBack: ExperimentAction
}