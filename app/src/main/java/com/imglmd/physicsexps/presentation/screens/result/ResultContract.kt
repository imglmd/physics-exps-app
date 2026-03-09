package com.imglmd.physicsexps.presentation.screens.result

import com.imglmd.physicsexps.domain.model.ExperimentResult

sealed interface ResultState{
    object Loading: ResultState
    data class Success(val result: ExperimentResult): ResultState
    data class Error(val message: String): ResultState
}

sealed interface ResultIntent{
    object Save: ResultIntent
    object Delete: ResultIntent
}


sealed interface ResultAction {
    data object NavigateBack: ResultAction
    data object NavigateHome: ResultAction
}