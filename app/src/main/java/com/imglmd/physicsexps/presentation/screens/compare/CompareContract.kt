package com.imglmd.physicsexps.presentation.screens.compare

import com.imglmd.physicsexps.domain.model.ExperimentResult

import com.imglmd.physicsexps.presentation.model.InputItem

interface CompareContract {

    sealed interface State {
        data object Loading: State
        data class Success(val items: List<CompareItem>): State
        data class Error(val message: String): State
    }

    sealed interface Intent {
        data object Back: Intent
    }

    sealed interface Action {
        data object NavigateBack: Action
    }
}
data class CompareItem(
    val experimentName: String,
    val date: Long,
    val inputs: List<InputItem>,
    val result: ExperimentResult
)