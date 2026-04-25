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
        val result = resultRepository.get()
        if (result != null) {
            _state.value = ResultContract.State.Success(result)

            if (runId == null) {
                saveRun(result)
            }
        } else {
            _state.value = ResultContract.State.Error("Результат не найден")
        }
    }

    fun onIntent(intent: ResultContract.Intent) {
        when (intent) {
            ResultContract.Intent.DeleteAndGoHome -> deleteRunAndNavigate(home = true)
            ResultContract.Intent.DeleteAndGoBack -> handleBack()
            ResultContract.Intent.ChangeInputs -> goToChangeInputs()
        }
    }

    private fun saveRun(result: ExperimentResult) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result) }
                .onSuccess { savedRunId = it }
                .onFailure {
                    Log.e("ResultViewModel", "Ошибка сохранения", it)
                }
        }
    }
    private fun goToChangeInputs(){
        viewModelScope.launch {
            if (runId == null) {
                handleBack()
                return@launch
            }
            val run = getRunUseCase(runId)
            Log.d("JSON", run.inputData)

            val inputs: Map<String, String> = Gson().fromJson(
                run.inputData,
                object : TypeToken<Map<String, String>>() {}.type
            )
            deleteRunInternal{  //TODO: заменить на чета хз что
                _effect.send(ResultContract.Effect.NavigateExperiment(
                    id = run.experimentId,
                    inputs = inputs
                ))
            }

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

    private fun deleteRunAndNavigate(home: Boolean) {
        viewModelScope.launch {
            deleteRunInternal {
                _effect.send(
                    if (home) {
                        ResultContract.Effect.NavigateHome
                    } else {
                        ResultContract.Effect.NavigateBack
                    }
                )
            }
        }
    }

    private suspend fun deleteRunInternal(onSuccess: suspend () -> Unit) {
        val id = savedRunId ?: return

        runCatching { deleteRunUseCase(id) }
            .onSuccess { onSuccess() }
            .onFailure {
                Log.e("ResultViewModel", "Ошибка удаления", it)
            }
    }
}