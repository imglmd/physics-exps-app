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
    preselectedIds: List<Int>,
    private val registry: ExperimentRegistry,
    private val getResultUseCase: GetResultUseCase,
    private val getRunUseCase: GetRunUseCase,
    private val resultRepository: InMemoryResultRepository,
    private val deleteAllRunsUseCase: DeleteAllRunsUseCase,
    private val getFilteredRunsUseCase: GetFilteredRunsUseCase,
    private val getExperimentsUseCase: GetAllExperimentsUseCase
) : ViewModel() {

    private val initialSelectedIds = preselectedIds.toSet()

    private val _state = MutableStateFlow<HistoryContract.State>(
        HistoryContract.State.Loading
    )
    val state = _state.asStateFlow()

    private val _filter = MutableStateFlow(HistoryFilter())

    private val _actionFlow = MutableSharedFlow<HistoryContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        if (preselectedIds.isNotEmpty()) {
            viewModelScope.launch {
                val firstRun = getRunUseCase(preselectedIds.first())

                _filter.value = _filter.value.copy(
                    experimentId = firstRun.experimentId
                )

                loadHistory()
            }
        } else {
            loadHistory()
        }
    }

    fun onIntent(intent: HistoryContract.Intent) {
        when (intent) {
            is HistoryContract.Intent.NavigateToResult -> navigateToResult(intent.resultId)

            HistoryContract.Intent.ShowDeleteDialog -> updateState {
                it.copy(showDeleteDialog = true)
            }

            HistoryContract.Intent.HideDeleteDialog -> updateState {
                it.copy(showDeleteDialog = false)
            }

            HistoryContract.Intent.DeleteAll -> deleteAllHistory()

            is HistoryContract.Intent.SetDateRange ->
                _filter.update { it.copy(dateFrom = intent.from, dateTo = intent.to) }

            is HistoryContract.Intent.SetExperimentFilter ->
                _filter.update { it.copy(experimentId = intent.experimentId) }

            is HistoryContract.Intent.SetSortOrder ->
                _filter.update { it.copy(sortOrder = intent.order) }

            HistoryContract.Intent.ClearFilters ->
                _filter.value = HistoryFilter()

            HistoryContract.Intent.ToggleFilterSheet -> updateState {
                it.copy(isFilterOpen = !it.isFilterOpen)
            }

            HistoryContract.Intent.ConfirmSelection -> {
                val st = _state.value as? HistoryContract.State.Success ?: return
                viewModelScope.launch {
                    _actionFlow.emit(
                        HistoryContract.Action.ReturnSelection(st.selectedIds.toList())
                    )
                }
            }

            is HistoryContract.Intent.ToggleSelection ->
                handleToggleSelection(intent.id)
        }
    }

    private fun handleToggleSelection(id: Int) {
        updateState { st ->

            val newSet = st.selectedIds.toMutableSet()
            val clickedItem = st.history.find { it.id == id } ?: return@updateState st

            if (newSet.contains(id)) {
                newSet.remove(id)

                if (newSet.isEmpty()) {
                    _filter.update { it.copy(experimentId = null) }
                }

                return@updateState st.copy(selectedIds = newSet)
            }

            if (newSet.size >= 2) return@updateState st

            if (newSet.isEmpty()) {
                _filter.update {
                    it.copy(experimentId = clickedItem.experimentId)
                }
            }

            val selectedItems = st.history.filter { newSet.contains(it.id) }

            val isSameExperiment = selectedItems.all {
                it.experimentId == clickedItem.experimentId
            }

            if (!isSameExperiment) return@updateState st

            newSet.add(id)

            st.copy(selectedIds = newSet)
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {

            val experiments = getExperimentsUseCase()

            getFilteredRunsUseCase(_filter)
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->

                    val prev = _state.value as? HistoryContract.State.Success

                    val selected = prev?.selectedIds ?: initialSelectedIds

                    _state.value = HistoryContract.State.Success(
                        history = emptyList(),
                        isLoading = true,
                        availableExperiments = experiments,
                        filter = _filter.value,
                        selectedIds = selected
                    )

                    val resultList = mutableListOf<HistoryItemUi>()

                    runs.chunked(5).forEach { chunk ->

                        val processed = chunk.mapNotNull { run ->
                            runCatching { processRun(run) }.getOrNull()
                        }

                        resultList += processed

                        _state.value = HistoryContract.State.Success(
                            history = resultList.toList(),
                            isLoading = true,
                            availableExperiments = experiments,
                            filter = _filter.value,
                            selectedIds = selected
                        )
                    }

                    _state.value = HistoryContract.State.Success(
                        history = resultList,
                        isLoading = false,
                        availableExperiments = experiments,
                        filter = _filter.value,
                        selectedIds = selected
                    )
                }
        }
    }

    private fun updateState(block: (HistoryContract.State.Success) -> HistoryContract.State.Success) {
        _state.update {
            (it as? HistoryContract.State.Success)?.let(block) ?: it
        }
    }

    private fun navigateToResult(id: Int) {
        viewModelScope.launch {
            val run = getRunUseCase(id) ?: return@launch

            val inputs = runCatching {
                Json.decodeFromString<Map<String, Double>>(run.inputData)
            }.getOrDefault(emptyMap())

            val result = getResultUseCase(id) ?: return@launch

            resultRepository.save(result, inputs)

            _actionFlow.emit(HistoryContract.Action.NavigateToResult(id))
        }
    }

    private suspend fun processRun(run: ExperimentRun): HistoryItemUi {
        val inputs = runCatching {
            Json.decodeFromString<Map<String, Double>>(run.inputData)
        }.getOrDefault(emptyMap())

        val experiment = runCatching {
            registry.getById(run.experimentId)
        }.getOrNull()

        val result = getResultUseCase(run.id)

        return HistoryItemUi(
            id = run.id,
            experimentId = run.experimentId,
            experimentName = experiment?.name ?: run.experimentId,
            category = experiment?.category ?: "",
            date = run.date,
            inputs = inputs,
            points = normalizePoints(
                downsamplePoints(result?.points ?: emptyList(), 30)
            ),
            quantities = result?.quantities ?: emptyList()
        )
    }

    private fun deleteAllHistory() {
        viewModelScope.launch {
            deleteAllRunsUseCase()
            updateState {
                it.copy(
                    showDeleteDialog = false,
                    history = emptyList()
                )
            }
        }
    }
}