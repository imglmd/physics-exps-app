package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val runId: Int?,
    private val resultRepository: InMemoryResultRepository,
    private val saveRunUseCase: SaveRunUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    private val getRunUseCase: GetRunUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var savedRunId: Int? = runId

    init {
        val bundle = resultRepository.get()

        if (bundle != null) {
            _state.value = ResultContract.State.Success(bundle.result)

            if (runId == null) {
                saveRun(bundle.result, bundle.inputs)
            }
        } else {
            _state.value = ResultContract.State.Error("Результат не найден")
        }
    }

    fun onIntent(intent: ResultContract.Intent) {
        when (intent) {
            ResultContract.Intent.Back -> handleBack()
            ResultContract.Intent.Delete -> handleDelete()
            ResultContract.Intent.Change -> handleChange()
        }
    }

    private fun handleBack() {
        viewModelScope.launch {
            if (runId == null) {
                deleteRunInternal {
                    _effect.send(ResultContract.Effect.NavigateBack)
                }
            } else {
                _effect.send(ResultContract.Effect.NavigateBack)
            }
        }
    }


    private fun handleDelete() {
        viewModelScope.launch {
            deleteRunInternal {
                _effect.send(ResultContract.Effect.NavigateHome)
            }
        }
    }


    private fun handleChange() {
        viewModelScope.launch {
            if (runId == null) {
                deleteRunInternal {
                    _effect.send(ResultContract.Effect.NavigateBack)
                }
            } else {
                val id = savedRunId ?: return@launch
                runCatching { getRunUseCase(id) }
                    .onSuccess { run ->
                        val inputs: Map<String, Double> = Gson().fromJson(
                            run.inputData,
                            object : TypeToken<Map<String, Double>>() {}.type
                        )
                        val stringInputs = inputs.mapValues { it.value.toString() }
                        deleteRunInternal {
                            _effect.send(
                                ResultContract.Effect.NavigateExperiment(
                                    id = run.experimentId,
                                    inputs = stringInputs
                                )
                            )
                        }
                    }
                    .onFailure {
                        Log.e("ResultViewModel", "Ошибка получения run", it)
                    }
            }
        }
    }



    private fun saveRun(
        result: ExperimentResult,
        inputs: Map<String, Double>
    ) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result, inputs) }
                .onSuccess { savedRunId = it }
                .onFailure { Log.e("ResultViewModel", "Ошибка сохранения", it) }
        }
    }

    private suspend fun deleteRunInternal(onSuccess: suspend () -> Unit) {
        val id = savedRunId ?: return
        runCatching { deleteRunUseCase(id) }
            .onSuccess { onSuccess() }
            .onFailure { Log.e("ResultViewModel", "Ошибка удаления", it) }
    }
}