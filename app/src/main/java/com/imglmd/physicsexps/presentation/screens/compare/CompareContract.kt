package com.imglmd.physicsexps.presentation.screens.compare

import com.imglmd.physicsexps.domain.model.ExperimentRun

interface CompareContract {
    sealed interface State {
        data object Loading: State
        data class Success(
            val runs: List<ExperimentRun>
        ): State
        data class Error(val message: String): State
    }
}