package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.domain.usecase.GetAllExperimentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getAllExperimentsUseCase: GetAllExperimentsUseCase
) : ViewModel() {

    private val allExperiments = getAllExperimentsUseCase()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        updateExperiments("")
    }

    fun onSearchTextChange(text: String) {
        updateExperiments(text)
    }

    private fun updateExperiments(search: String) {

        val filtered = allExperiments.filter {
            it.name.contains(search, ignoreCase = true) ||
            it.category.contains(search, ignoreCase = true) ||
            it.description.contains(search, ignoreCase = true)
        }

        val grouped = filtered.groupBy { it.category }

        _state.update {
            it.copy(
                searchText = search,
                experimentsByCategory = grouped
            )
        }
    }
}