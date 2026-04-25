package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.GetAllRunsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getAllExperimentsUseCase: GetAllExperimentsUseCase,
    private val getAllRunsUseCase: GetAllRunsUseCase,
    private val registry: ExperimentRegistry
) : ViewModel() {

    private val allExperiments = getAllExperimentsUseCase()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        updateExperiments("")
        loadHistory()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeSearchText -> updateExperiments(intent.text)
        }
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

    private fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val runs = getAllRunsUseCase()
            val historyUi = runs.map { run ->
                val name = runCatching { registry.getById(run.experimentId).name }
                    .getOrDefault(run.experimentId)
                HistoryItemUi(
                    id = run.id,
                    experimentName = name,
                    category = runCatching { registry.getById(run.experimentId).category }
                        .getOrDefault(""),
                    date = run.date
                )
            }
            _state.update { it.copy(history = historyUi) }
        }
    }

}