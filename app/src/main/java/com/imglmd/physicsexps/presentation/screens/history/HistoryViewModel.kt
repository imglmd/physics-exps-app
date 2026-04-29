package com.imglmd.physicsexps.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetFilteredRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.presentation.downsamplePoints
import com.imglmd.physicsexps.presentation.model.HistoryFilter
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import com.imglmd.physicsexps.presentation.normalizePoints
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

class HistoryViewModel(
    private val registry: ExperimentRegistry,
    private val getResultUseCase: GetResultUseCase,
    private val getRunUseCase: GetRunUseCase,
    private val resultRepository: InMemoryResultRepository,
    private val deleteAllRunsUseCase: DeleteAllRunsUseCase,
    private val getFilteredRunsUseCase: GetFilteredRunsUseCase,
    private val getExperimentsUseCase: GetAllExperimentsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryContract.State>(
        HistoryContract.State.Loading
    )
    val state = _state.asStateFlow()

    private val _filter = MutableStateFlow(HistoryFilter())

    private val _actionFlow = MutableSharedFlow<HistoryContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()


    init {
        loadHistory()

        viewModelScope.launch {
            _filter.collect { filter ->
                _state.update {
                    (it as? HistoryContract.State.Success)?.copy(
                        filter = filter
                    ) ?: it
                }
            }
        }
    }

    fun onIntent(intent: HistoryContract.Intent){
        when (intent) {
            is HistoryContract.Intent.NavigateToResult -> navigateToResult(intent.resultId)

            HistoryContract.Intent.ShowDeleteDialog -> _state.update {
                (it as? HistoryContract.State.Success)?.copy(showDeleteDialog = true) ?: it
            }
            HistoryContract.Intent.HideDeleteDialog -> _state.update {
                (it as? HistoryContract.State.Success)?.copy(showDeleteDialog = false) ?: it
            }
            HistoryContract.Intent.DeleteAll -> deleteAllHistory()

            is HistoryContract.Intent.SetDateRange -> _filter.update { it.copy(dateFrom = intent.from, dateTo = intent.to) }
            is HistoryContract.Intent.SetExperimentFilter -> _filter.update { it.copy(experimentId = intent.experimentId) }
            is HistoryContract.Intent.SetSortOrder -> _filter.update { it.copy(sortOrder = intent.order) }
            HistoryContract.Intent.ClearFilters -> _filter.value = HistoryFilter()
            HistoryContract.Intent.ToggleFilterSheet -> _state.update {
                (it as? HistoryContract.State.Success)?.copy(isFilterOpen = !it.isFilterOpen) ?: it
            }
        }
    }

    private fun navigateToResult(id: Int){
        viewModelScope.launch {
            val run = getRunUseCase(id) ?: return@launch

            val inputs: Map<String, Double> =
                runCatching {
                    Json.decodeFromString<Map<String, Double>>(run.inputData)
                }.getOrDefault(emptyMap())

            val result = getResultUseCase(id) ?: return@launch

            resultRepository.save(result, inputs)

            _actionFlow.emit(HistoryContract.Action.NavigateToResult(id))
        }
    }
    private fun loadHistory() {
        viewModelScope.launch {

            val experiments = getExperimentsUseCase()

            getFilteredRunsUseCase(_filter)
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->

                    _state.value = HistoryContract.State.Success(
                        history = emptyList(),
                        isLoading = true,
                        availableExperiments = experiments,
                        filter = _filter.value
                    )

                    val chunkSize = 5
                    val resultList = mutableListOf<HistoryItemUi>()

                    runs.chunked(chunkSize).forEach { chunk ->

                        val processed = chunk.mapNotNull { run ->
                            runCatching { processRun(run) }.getOrNull()
                        }

                        resultList += processed

                        _state.value = HistoryContract.State.Success(
                            history = resultList.toList(),
                            isLoading = true,
                            availableExperiments = experiments,
                            filter = _filter.value
                        )
                    }

                    _state.value = HistoryContract.State.Success(
                        history = resultList,
                        isLoading = false,
                        availableExperiments = experiments,
                        filter = _filter.value
                    )
                }
        }
    }


    private suspend fun processRun(run: ExperimentRun): HistoryItemUi {
        val inputs: Map<String, Double> = runCatching {
            Json.decodeFromString<Map<String, Double>>(run.inputData)
        }.getOrDefault(emptyMap())

        val experiment = runCatching { registry.getById(run.experimentId) }.getOrNull()
        val result = getResultUseCase(run.id)

        return HistoryItemUi(
            id = run.id,
            experimentName = experiment?.name ?: run.experimentId,
            category = experiment?.category ?: "",
            date = run.date,
            inputs = inputs,
            points = normalizePoints(downsamplePoints(result?.points ?: emptyList(), 30)),
            quantities = result?.quantities ?: emptyList()
        )
    }

    private fun deleteAllHistory(){
        viewModelScope.launch {
            deleteAllRunsUseCase()
            _state.update {
                (it as? HistoryContract.State.Success)?.copy(
                    showDeleteDialog = false,
                    history = emptyList()
                ) ?: it
            }
        }
    }
}