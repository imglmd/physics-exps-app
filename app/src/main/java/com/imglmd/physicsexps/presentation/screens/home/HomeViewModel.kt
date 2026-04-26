package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getAllExperimentsUseCase: GetAllExperimentsUseCase,
    private val getAllRunsUseCase: GetAllRunsUseCase,
    private val getResultUseCase: GetResultUseCase,
    private val registry: ExperimentRegistry,
    private val resultRepository: InMemoryResultRepository
) : ViewModel() {

    private val allExperiments = getAllExperimentsUseCase()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<HomeAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        updateExperiments("")
        loadHistory()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeSearchText -> updateExperiments(intent.text)
            is HomeIntent.NavigateToRunResult -> navigateToResult(intent.id)
            is HomeIntent.NavigateToExperiment -> navigateToExperiment(intent.id)
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
    private fun navigateToExperiment(id: String){
        viewModelScope.launch {
            _actionFlow.emit(HomeAction.NavigateToExperiment(id))
        }
    }
    private fun navigateToResult(id: Int){
        viewModelScope.launch {
            val result = getResultUseCase(id) ?: return@launch
            resultRepository.save(result)
            _actionFlow.emit(HomeAction.NavigateToResult(id))
        }

    }

    private fun loadHistory() {
        viewModelScope.launch {
            getAllRunsUseCase()
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->
                    val historyUi = runs.map { run ->
                        val type = object : TypeToken<Map<String, Double>>() {}.type

                        val inputs: Map<String, Double> = runCatching {
                            Gson().fromJson<Map<String, Double>>(run.inputData, type)
                        }.getOrDefault(emptyMap())

                        HistoryItemUi(
                            id = run.id,
                            experimentName = runCatching { registry.getById(run.experimentId).name }
                                .getOrDefault(run.experimentId),
                            category = runCatching { registry.getById(run.experimentId).category }
                                .getOrDefault(""),
                            date = run.date,
                            resultId = run.resultId,
                            inputs = inputs
                        )
                    }
                    _state.update { it.copy(history = historyUi) }
                }
        }
    }


}