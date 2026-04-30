package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.validation.ValidationError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExperimentViewModel(
    private val id: String,
    private val inputs: Map<String, String>?,
    private val replaceRunId: Int?,
    private val getExperiment: GetExperimentByIdUseCase,
    private val calculate: CalculateExperimentUseCase,
    private val resultRepository: InMemoryResultRepository
) : ViewModel() {

    private val experiment = getExperiment(id)

    private val _state = MutableStateFlow(
        ExperimentContract.State(
            experiment = experiment,
            inputs = inputs?: emptyMap(),
            isAdvancedMode = hasAdvancedInputs(inputs)
        )
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: ExperimentContract.Intent) {
        when (intent) {
            is ExperimentContract.Intent.ChangeValue -> changeValue(intent.key, intent.newValue)
            ExperimentContract.Intent.Start -> start()
            ExperimentContract.Intent.ToggleAdvancedMode -> _state.update { it.copy(isAdvancedMode = !it.isAdvancedMode) }
        }
    }

    private fun start() = viewModelScope.launch {

        _state.update { it.copy(isLoading = true, error = null) }

        when (val result = calculate(id, state.value.inputs)) {

            is CalculateExperimentUseCase.Result.Success -> {
                resultRepository.save(result.result, result.inputs, replaceRunId)
                _actionFlow.emit(ExperimentContract.Action.NavigateToResult)
            }

            is CalculateExperimentUseCase.Result.ValidationError -> {
                _state.update {
                    it.copy(
                        error = mapValidationErrors(result.errors)
                    )
                }
            }

            is CalculateExperimentUseCase.Result.Failure -> {
                _state.update {
                    it.copy(error = result.message)
                }
            }
        }

        _state.update { it.copy(isLoading = false) }
    }

    private fun hasAdvancedInputs(inputs: Map<String, String>?): Boolean {
        if (inputs.isNullOrEmpty()) return false

        val additionalKeys = experiment.additionalInputFields.map { it.key }

        return inputs.any { (key, value) ->
            key in additionalKeys && value.toDoubleOrNull() != null
        }
    }
    private fun changeValue(key: String, newValue: String) {
        _state.update { current ->

            val newInputs = current.inputs + (key to newValue)
            val allFields = current.experiment.inputFields + current.experiment.additionalInputFields

            val allRequiredFilled = allFields
                .filter { it.required }
                .all { field ->
                    newInputs[field.key]?.toDoubleOrNull() != null
                }

            val validCount = newInputs.values.count { it.toDoubleOrNull() != null }

            val isEnoughInputs = validCount >= experiment.minRequiredInputs

            current.copy(
                inputs = newInputs,
                error = null,
                isButtonActive = allRequiredFilled && isEnoughInputs
            )
        }
    }
    private fun mapValidationErrors(errors: List<ValidationError>): String {

        val first = errors.firstOrNull() ?: return "Ошибка ввода"

        return when (first) {

            is ValidationError.NotEnoughInputs ->
                "Введите минимум ${experiment.minRequiredInputs} значения"

            is ValidationError.InvalidNumber ->
                "Некорректное число"

            is ValidationError.OutOfRange ->
                "Значение вне допустимого диапазона"

            is ValidationError.RequiredField ->
                "Заполните обязательные поля"
        }
    }
}