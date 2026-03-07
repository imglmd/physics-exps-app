package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.GetExperimentByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExperimentViewModel(
    private val id: String,
    private val getExperiment: GetExperimentByIdUseCase,
    private val calculate: CalculateExperimentUseCase,
    private val resultRepository: InMemoryResultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ExperimentContract.State(getExperiment(id))
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: ExperimentContract.Intent) {
        when (intent) {
            is ExperimentContract.Intent.ChangeValue -> changeValue(intent.key, intent.newValue)

            ExperimentContract.Intent.Start -> start()
        }
    }

    private fun start() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val parsedInputs = parseInputs(state.value.inputs)
        if (parsedInputs == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Некорректное число",
                    isButtonActive = false
                )
            }
            return@launch
        }

        delay(1000) // TODO убрать

        calculate(id, parsedInputs)
            .onSuccess { result ->
                resultRepository.save(result)
                _actionFlow.emit(ExperimentContract.Action.NavigateToResult)
            }
            .onFailure { error ->
                _state.update {
                    it.copy(
                        error = error.message,
                        isButtonActive = false
                    )
                }
            }

        _state.update { it.copy(isLoading = false) }
    }

    private fun changeValue(key: String, newValue: String) {
        _state.update { current ->
            val newInputs = current.inputs + (key to newValue)

            current.copy(
                inputs = newInputs,
                error = null,
                isButtonActive = validateInputs(newInputs)
            )
        }
    }

    private fun parseInputs(inputs: Map<String, String>): Map<String, Double>? {
        val parsed = inputs.mapValues { (_, value) ->
            value.toDoubleOrNull() ?: return null
        }
        return parsed
    }

    private fun validateInputs(inputs: Map<String, String>): Boolean {
        if (inputs.isEmpty()) return false

        return inputs.values.any {
            it.isNotBlank() && it.toDoubleOrNull() != null
        }
    }
}