package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetLastRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeViewModel(
    getAllExperimentsUseCase: GetAllExperimentsUseCase,
    private val getLastRunsUseCase: GetLastRunsUseCase,
    private val getResultUseCase: GetResultUseCase,
    private val registry: ExperimentRegistry,
    private val getRunUseCase: GetRunUseCase,
    private val resultRepository: InMemoryResultRepository
) : ViewModel() {

    private val allExperiments = getAllExperimentsUseCase()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<HomeAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    private val json = Json

    init {
        updateExperiments("")
        loadHistory()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeSearchText -> updateExperiments(intent.text)
            is HomeIntent.NavigateToRunResult -> navigateToResult(intent.id)
            is HomeIntent.NavigateToExperiment -> navigateToExperiment(intent.id)
            HomeIntent.NavigateToHistory -> navigateToHistory()
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

            val run = getRunUseCase(id) ?: return@launch

            val inputs: Map<String, Double> =
                runCatching {
                    json.decodeFromString<Map<String, Double>>(run.inputData)
                }.getOrDefault(emptyMap())

            val result = getResultUseCase(id) ?: return@launch

            resultRepository.save(result, inputs)
            _actionFlow.emit(HomeAction.NavigateToResult(id))
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getLastRunsUseCase(HISTORY_LIMIT + 1)
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->
                    val hasMore = runs.size > HISTORY_LIMIT
                    val visibleRuns = runs.take(HISTORY_LIMIT)

                    val historyUi = visibleRuns.map { run ->
                        val inputs: Map<String, Double> =
                            runCatching {
                                json.decodeFromString<Map<String, Double>>(run.inputData)
                            }.getOrDefault(emptyMap())

                        val experiment = runCatching {
                            registry.getById(run.experimentId)
                        }.getOrNull()

                        HistoryItemUi(
                            id = run.id,
                            experimentId = experiment?.id ?: "unknown",
                            experimentName = experiment?.name ?: run.experimentId,
                            category = experiment?.category ?: "",
                            date = run.date,
                            inputs = inputs
                        )
                    }

                    _state.update {
                        it.copy(
                            history = historyUi,
                            hasMoreHistory = hasMore,
                            isHistoryLoaded = true
                        )
                    }
                }
        }
    }


    private fun navigateToHistory() {
        viewModelScope.launch {
            _actionFlow.emit(HomeAction.NavigateToHistory)
        }
    }

    private companion object {
        const val HISTORY_LIMIT = 6
    }

}