package com.imglmd.physicsexps.presentation.screens.home

import com.imglmd.physicsexps.domain.model.Experiment


data class HomeState(
    val experimentsByCategory: Map<String, List<Experiment>> = emptyMap(),
    val history: List<Nothing> = emptyList(), // TODO
    val searchText: String = ""
)
sealed interface HomeIntent{
    data class ChangeSearchText(val text: String): HomeIntent
}