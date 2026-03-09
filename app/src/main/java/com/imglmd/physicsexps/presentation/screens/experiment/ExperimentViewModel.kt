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

    private val experiment = getExperiment(id)

    private val _state = MutableStateFlow(
        ExperimentState(experiment)
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: ExperimentIntent) {
        when (intent) {
            is ExperimentIntent.ChangeValue -> changeValue(intent.key, intent.newValue)

            ExperimentIntent.Start -> start()
        }
    }

    private fun start() = viewModelScope.launch {

        val parsed = parseInputs(state.value.inputs)

        if (parsed.size < experiment.minRequiredInputs) {
            _state.update {
                it.copy(error = "Введите минимум ${experiment.minRequiredInputs} значения")
            }
            return@launch
        }

        _state.update { it.copy(isLoading = true, error = null) }

        delay(500) //TODO убрать

        calculate(id, parsed)
            .onSuccess { result ->
                resultRepository.save(result)
                _actionFlow.emit(ExperimentAction.NavigateToResult)
            }
            .onFailure { error ->
                _state.update {
                    it.copy(error = error.message ?: "Ошибка вычисления")
                }
            }

        _state.update { it.copy(isLoading = false) }
    }

    private fun changeValue(key: String, newValue: String) {
        _state.update { current ->

            val newInputs = current.inputs + (key to newValue)

            val parsed = parseInputs(newInputs)

            current.copy(
                inputs = newInputs,
                error = null,
                isButtonActive = parsed.size >= experiment.minRequiredInputs
            )
        }
    }

    private fun parseInputs(inputs: Map<String, String>): Map<String, Double> {
        return inputs.mapNotNull { (key, value) ->
            value.toDoubleOrNull()?.let { key to it }
        }.toMap()
    }
}