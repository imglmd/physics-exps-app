package com.imglmd.physicsexps.presentation.screens.home

import com.imglmd.physicsexps.domain.model.Experiment
import com.imglmd.physicsexps.presentation.model.HistoryItemUi


data class HomeState(
    val experimentsByCategory: Map<String, List<Experiment>> = emptyMap(),
    val history: List<HistoryItemUi> = emptyList(),
    val hasMoreHistory: Boolean = false,
    val searchText: String = "",
    val isHistoryLoaded: Boolean = false
)
sealed interface HomeIntent{
    data class ChangeSearchText(val text: String): HomeIntent
    data class NavigateToRunResult(val id: Int): HomeIntent
    data class NavigateToExperiment(val id: String): HomeIntent
    data object NavigateToHistory: HomeIntent
}
sealed interface HomeAction {
    data class NavigateToResult(val runId: Int): HomeAction
    data class NavigateToExperiment(val id: String): HomeAction
    data object NavigateToHistory: HomeAction

}