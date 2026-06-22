package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.core.OnlineStateManager
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.physicsexps.domain.validation.ExperimentValidator
import com.imglmd.physicsexps.domain.validation.ValidationError
import com.imglmd.physicsexps.domain.validation.ValidationResult
import com.imglmd.physicsexps.feature.settings.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExperimentViewModel(
    private val id: String,
    private val inputs: Map<String, String>?,
    private val replaceRunId: Int?,
    private val getExperiment: GetExperimentByIdUseCase,
    private val calculate: CalculateExperimentUseCase,
    private val resultRepository: InMemoryResultRepository,
    private val validator: ExperimentValidator,
    private val getExperimentImagesUseCase: GetExperimentImagesUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val onlineStateManager: OnlineStateManager,
) : ViewModel() {

    private val experiment = getExperiment(id)

    private val initialInputs = inputs ?: emptyMap()

    private val initialValidation = validator.validate(experiment, initialInputs)

    private val _state = MutableStateFlow(
        ExperimentContract.State(
            experiment = experiment,
            inputs = initialInputs,
            isAdvancedMode = hasAdvancedInputs(initialInputs),
            isButtonActive = initialValidation is ValidationResult.Success
        )
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val isAdvanced = getSettingsUseCase().first().advancedMode
            _state.update { it.copy(isAdvancedMode = isAdvanced) }
        }
        loadImages()
    }

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

            val validation = validator.validate(
                experiment = current.experiment,
                rawInputs = newInputs
            )

            current.copy(
                inputs = newInputs,
                error = null,
                isButtonActive = validation is ValidationResult.Success
            )
        }
    }
    private fun mapValidationErrors(errors: List<ValidationError>): String {
        return when (errors.firstOrNull()) {
            ValidationError.NotEnoughInputs ->
                "Недостаточно данных"

            ValidationError.InvalidCombination ->
                "Некорректная комбинация значений"

            is ValidationError.InvalidNumber ->
                "Некорректное число"

            is ValidationError.OutOfRange ->
                "Значение вне допустимого диапазона"

            is ValidationError.RequiredField ->
                "Заполните обязательные поля"

            null ->
                "Ошибка ввода"
        }
    }

    private fun loadImages() {
        if (!onlineStateManager.state.value.canUseOnlineFeatures) {
            _state.update { it.copy(isImagesLoading = false) }
            return
        }
        viewModelScope.launch {
            getExperimentImagesUseCase(id)
                .onSuccess { imageUrls ->
                    _state.update {
                        it.copy(
                            imageUrls = imageUrls,
                            isImagesLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            imageUrls = emptyList(),
                            isImagesLoading = false
                        )
                    }
                }
        }
    }
}
