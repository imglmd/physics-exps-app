package com.imglmd.physicsexps.presentation.screens.result

import com.imglmd.physicsexps.domain.model.ExperimentResult

interface ResultContract {
    sealed interface State{
        object Loading: State
        data class Success(val result: ExperimentResult): State
        data class Error(val message: String): State
    }
    sealed interface Intent {
        object DeleteAndGoHome: Intent
        object DeleteAndGoBack: Intent
        object ChangeInputs: Intent
    }

    sealed interface Effect {
        object NavigateBack: Effect
        object NavigateHome: Effect
        data class NavigateExperiment(val id: String, val inputs: Map<String, String>): Effect
    }
}