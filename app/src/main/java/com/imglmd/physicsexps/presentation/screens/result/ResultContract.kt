package com.imglmd.physicsexps.presentation.screens.result

import com.imglmd.physicsexps.domain.model.ExperimentResult

object ResultContract {

    sealed interface State {
        data object Loading: State
        data class Success(val result: ExperimentResult): State
        data class Error(val message: String): State
    }

    sealed interface Intent {
        data object Back: Intent
        data object Delete: Intent
        data object Change: Intent
    }

    sealed interface Effect {
        data object NavigateBack: Effect
        data object NavigateHome: Effect
        data class NavigateExperiment(val id: String, val inputs: Map<String, String>): Effect
    }
}