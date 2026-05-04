package com.imglmd.physicsexps.presentation.screens.compare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.presentation.model.InputItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CompareViewModel(
    private val runIds: List<Int>,
    private val getRunUseCase: GetRunUseCase,
    private val getResultUseCase: GetResultUseCase,
    private val registry: ExperimentRegistry
): ViewModel() {

    private val _state = MutableStateFlow<CompareContract.State>(CompareContract.State.Loading)
    val state = _state.asStateFlow()

    private val _action = Channel<CompareContract.Action>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    init {
        load()
    }

    fun onIntent(intent: CompareContract.Intent) {
        when (intent) {
            CompareContract.Intent.Back -> viewModelScope.launch { _action.send(CompareContract.Action.NavigateBack) }
        }
    }

    private fun load(){
        viewModelScope.launch {
            runCatching {
                val items = runIds.mapNotNull { runId ->

                    val run = getRunUseCase(runId)
                    val result = getResultUseCase(runId) ?: return@mapNotNull null
                    val experiment = registry.getById(run.experimentId)

                    val inputMap = parseInputs(run.inputData)

                    val allFields = experiment.inputFields + experiment.additionalInputFields

                    val inputs = allFields.map { field ->
                        InputItem(
                            label = field.label,
                            symbol = field.symbol,
                            unit = field.unit,
                            value = inputMap[field.key]
                        )
                    }
                    CompareItem (
                        experimentName = experiment.name,
                        date = run.date,
                        inputs = inputs,
                        result = result
                    )
                }
                items
            }.onSuccess { _state.value = CompareContract.State.Success(it)
                println("$it")
            }.onFailure { _state.value = CompareContract.State.Error(it.message ?: "Error") }
        }
    }

    private fun parseInputs(json: String): Map<String, Double> {
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyMap()
        }
    }
}