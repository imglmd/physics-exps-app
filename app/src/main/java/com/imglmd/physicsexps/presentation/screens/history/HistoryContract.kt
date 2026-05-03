package com.imglmd.physicsexps.presentation.screens.history

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.presentation.model.HistoryFilter
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import com.imglmd.physicsexps.presentation.model.SortOrder

interface HistoryContract {

    data class State (
        val history: List<HistoryItemUi> = emptyList(),
        val filter: HistoryFilter = HistoryFilter(),
        val availableExperiments: List<Experiment> = emptyList(),
        val isLoading: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val selectedIds: List<Int> = emptyList()
    )

    sealed interface Intent {
        data class NavigateToResult(val resultId: Int): Intent

        data class SetExperimentFilter(val experimentId: String?): Intent
        data class SetDateRange(val from: Long?, val to: Long?): Intent
        data class SetSortOrder(val order: SortOrder): Intent
        data object ClearFilters: Intent

        data object ShowDeleteDialog: Intent
        data object HideDeleteDialog: Intent
        data object DeleteAll: Intent

        data class ToggleSelection(val id: Int): Intent
        data object ConfirmSelection: Intent
    }

    sealed interface Action {
        data class NavigateToResult(val resultId: Int): Action
        data object NavigateBack: Action
        data class ReturnSelection(val ids: List<Int>): Action
    }
}