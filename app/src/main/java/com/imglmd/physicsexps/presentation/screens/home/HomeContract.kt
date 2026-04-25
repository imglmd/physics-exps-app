package com.imglmd.physicsexps.presentation.screens.home

import com.imglmd.physicsexps.domain.model.Experiment


data class HomeState(
    val experimentsByCategory: Map<String, List<Experiment>> = emptyMap(),
    val history: List<HistoryItemUi> = emptyList(),
    val searchText: String = ""
)
sealed interface HomeIntent{
    data class ChangeSearchText(val text: String): HomeIntent
    data class NavigateToRunResult(val id: Int): HomeIntent
    data class NavigateToExperiment(val id: String): HomeIntent
}
sealed interface HomeAction {
    data class NavigateToResult(val runId: Int): HomeAction
    data class NavigateToExperiment(val id: String): HomeAction
}