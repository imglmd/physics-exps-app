package com.imglmd.physicsexps.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val registry: ExperimentRegistry,
    private val getResultUseCase: GetResultUseCase,
    private val resultRepository: InMemoryResultRepository,
    private val getAllRunsUseCase: GetAllRunsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryContract.State>(
        HistoryContract.State.Loading
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<HistoryContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()
    private val gson = Gson()
    private val type = object : TypeToken<Map<String, Double>>() {}.type

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
            val result = getResultUseCase(id) ?: return@launch
            resultRepository.save(result)
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
                                gson.fromJson(run.inputData, type) ?: emptyMap()

                            val experiment = runCatching {
                                registry.getById(run.experimentId)
                            }.getOrNull()

                            HistoryItemUi(
                                id = run.id,
                                experimentName = experiment?.name ?: run.experimentId,
                                category = experiment?.category ?: "",
                                date = run.date,
                                resultId = run.resultId,
                                inputs = inputs
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