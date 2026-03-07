package com.imglmd.physicsexps.presentation.screens.experiment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.GetExperimentByIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExperimentViewModel(
    private val id: String,
    private val getExperiment: GetExperimentByIdUseCase,
    private val calculate: CalculateExperimentUseCase,
    private val resultRepository: InMemoryResultRepository
): ViewModel() {
    private val _state = MutableStateFlow(ExperimentContract.State(getExperiment(id)))
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()


    fun onIntent(intent: ExperimentContract.Intent){
        when(intent){
            is ExperimentContract.Intent.ChangeValue -> changeValue(intent.key, intent.newValue)
            ExperimentContract.Intent.Start -> start()
        }
    }

    private fun start() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val parsedInputs = state.value.inputs.mapValues { (_, value) ->
                value.toDoubleOrNull()
                    ?: return@launch _state.update { it.copy(isLoading = false, error = "Некорректное число") }
            }

            calculate(id, parsedInputs)
                .onSuccess { result ->
                    resultRepository.save(result)
                    if (resultRepository.get() == null){
                        println("схуяли null")
                    } else {
                        println(resultRepository.get()!!.quantities.toString())

                    }
                    _actionFlow.emit(ExperimentContract.Action.NavigateToResult)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun changeValue(key: String, newValue: String) {
        _state.update {
            it.copy(
                inputs = it.inputs + (key to newValue)
            )
        }
    }
}