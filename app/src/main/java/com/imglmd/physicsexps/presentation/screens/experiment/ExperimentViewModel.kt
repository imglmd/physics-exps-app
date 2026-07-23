package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.physicsexps.experiments.validation.ExperimentValidator
import com.imglmd.physicsexps.experiments.validation.ValidationError
import com.imglmd.physicsexps.experiments.validation.ValidationResult
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
    private val getSettingsUseCase: GetSettingsUseCase
): ViewModel() {

    private val experiment = getExperiment(id)


    private val cachedSettings = getSettingsUseCase.getCached()

    private val initialInputs = inputs ?: emptyMap()

    private val initialValidation = validator.validate(experiment, initialInputs)

    private val _state = MutableStateFlow(
        ExperimentContract.State(
            experiment = experiment,
            inputs = initialInputs,
            isAdvancedMode = hasAdvancedInputs(initialInputs) || cachedSettings?.advancedMode ?: false,
            isOfflineMode = cachedSettings?.offlineMode ?: false,
            isButtonActive = initialValidation is ValidationResult.Success
        )
    )
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ExperimentContract.Action>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val settings = getSettingsUseCase().first()
            _state.update { it.copy(
                isAdvancedMode = hasAdvancedInputs(initialInputs) || settings.advancedMode,
                isOfflineMode = settings.offlineMode
            ) }
            if (!settings.offlineMode) loadImages()
        }
    }

    fun onIntent(intent: ExperimentContract.Intent) {
        when (intent) {
            is ExperimentContract.Intent.ChangeValue -> changeValue(intent.key, intent.newValue)
            ExperimentContract.Intent.Start -> start()
            ExperimentContract.Intent.ToggleAdvancedMode -> _state.update { it.copy(isAdvancedMode = !it.isAdvancedMode) }
        }
    }

    private fun start() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, generalError = null, hasAttemptedSubmit = true) }

        when (val result = calculate(id, state.value.inputs)) {
            is CalculateExperimentUseCase.Result.Success -> {
                resultRepository.save(result.result, result.inputs, replaceRunId)
                _actionFlow.emit(ExperimentContract.Action.NavigateToResult)
            }
            is CalculateExperimentUseCase.Result.ValidationError -> {
                val (fieldErrors, generalError) = mapValidationErrors(result.errors)
                _state.update { it.copy(fieldErrors = fieldErrors, generalError = generalError) }
            }
            is CalculateExperimentUseCase.Result.Failure -> {
                _state.update { it.copy(generalError = result.message) }
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
            val validation = validator.validate(current.experiment, newInputs)

            if (!current.hasAttemptedSubmit) {
                return@update current.copy(
                    inputs = newInputs,
                    isButtonActive = validation is ValidationResult.Success
                )
            }
            val (fieldErrors, generalError) = if (validation is ValidationResult.Error) {
                mapValidationErrors(validation.errors)
            } else {
                emptyMap<String, String>() to null
            }

            current.copy(
                inputs = newInputs,
                fieldErrors = fieldErrors,
                generalError = generalError,
                isButtonActive = validation is ValidationResult.Success
            )
        }
    }
    private fun mapValidationErrors(errors: List<ValidationError>): Pair<Map<String, String>, String?> {
        val fieldErrors = mutableMapOf<String, String>()
        var generalError: String? = null

        for (error in errors) {
            when (error) {
                is ValidationError.RequiredField ->
                    fieldErrors[error.fieldKey] = "Обязательное поле"

                is ValidationError.InvalidNumber ->
                    fieldErrors[error.fieldKey] = "Введите число"

                is ValidationError.OutOfRange -> {
                    fieldErrors[error.fieldKey] = when {
                        error.min != null && error.max != null -> "От ${error.min} до ${error.max}"
                        error.min != null -> "Не меньше ${error.min}"
                        error.max != null -> "Не больше ${error.max}"
                        else -> "Значение вне диапазона"
                    }
                }

                ValidationError.NotEnoughInputs ->
                    generalError = "Недостаточно данных для расчёта"

                ValidationError.InvalidCombination ->
                    generalError = "Такая комбинация значений невозможна"
            }
        }

        return fieldErrors to generalError
    }

    private fun loadImages() {
        viewModelScope.launch {
            getExperimentImagesUseCase(id)
                .onSuccess { imageUrls ->
                    _state.update {
                        it.copy(
                            imageUrls = imageUrls,
                            isImagesError = false,
                            isImagesLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            imageUrls = emptyList(),
                            isImagesError = true,
                            isImagesLoading = false
                        )
                    }
                }
        }
    }
}
