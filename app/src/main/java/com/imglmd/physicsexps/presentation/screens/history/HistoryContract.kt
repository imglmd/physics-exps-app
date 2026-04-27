package com.imglmd.physicsexps.presentation.screens.history

import com.imglmd.physicsexps.presentation.model.HistoryItemUi

interface HistoryContract {

    sealed interface State {
        data object Loading: State
        data class Success(
            val history: List<HistoryItemUi>,
            val isLoading: Boolean = false,
            val showDeleteDialog: Boolean = false
        ): State
        data class Error(val message: String): State
    }

    sealed interface Intent {
        data class NavigateToResult(val resultId: Int): Intent

        data object ShowDeleteDialog : Intent
        data object HideDeleteDialog : Intent
        data object DeleteAll: Intent
    }

    sealed interface Action {
        data class NavigateToResult(val resultId: Int): Action
        data object NavigateBack: Action
    }
}