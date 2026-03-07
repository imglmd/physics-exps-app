package com.imglmd.physicsexps.presentation.screens.experiment

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.domain.model.ExperimentResult

interface ExperimentContract {
    data class State (
        val experiment: Experiment,
        val inputs: Map<String, String> = emptyMap(),
        val error: String? = null,
        val isLoading: Boolean = false,
        val isButtonActive: Boolean = false
    )
    sealed interface Intent {
        data object Start: Intent
        data class ChangeValue(val key: String, val newValue: String): Intent
    }
    sealed interface Action {
        data object NavigateToResult: Action
        data object NavigateBack: Action
    }
}