package com.imglmd.physicsexps.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.presentation.downsamplePoints
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import com.imglmd.physicsexps.presentation.normalizePoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HistoryViewModel(
    private val registry: ExperimentRegistry,
    private val getResultUseCase: GetResultUseCase,
    private val getRunUseCase: GetRunUseCase,
    private val resultRepository: InMemoryResultRepository,
    private val getAllRunsUseCase: GetAllRunsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryContract.State>(
        HistoryContract.State.Loading
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<HistoryContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    private val json = Json

    init {
        loadHistory()
    }

    fun onIntent(intent: HistoryContract.Intent){
        when (intent) {
            is HistoryContract.Intent.NavigateToResult -> navigateToResult(intent.resultId)
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

            _actionFlow.emit(HistoryContract.Action.NavigateToResult(id))
        }
    }
    private fun loadHistory() {
        viewModelScope.launch {
            getAllRunsUseCase()
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->
                    runCatching {

                        val historyUi = runs.map { run ->

                            val inputs: Map<String, Double> =
                                runCatching {
                                    json.decodeFromString<Map<String, Double>>(run.inputData)
                                }.getOrDefault(emptyMap())

                            val experiment = runCatching {
                                registry.getById(run.experimentId)
                            }.getOrNull()

                            val result = getResultUseCase(run.resultId)

                            HistoryItemUi(
                                id = run.id,
                                experimentName = experiment?.name ?: run.experimentId,
                                category = experiment?.category ?: "",
                                date = run.date,
                                resultId = run.resultId,
                                inputs = inputs,
                                points = normalizePoints(downsamplePoints(result?.points ?: emptyList(), 30)),
                                quantities = result?.quantities ?: emptyList()
                            )
                        }

                        _state.value = HistoryContract.State.Success(historyUi)

                    }.onFailure { e ->
                        _state.value = HistoryContract.State.Error(
                            e.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
}