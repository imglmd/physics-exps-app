package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: InMemoryResultRepository,
    private val saveRunUseCase: SaveRunUseCase,
    private val deleteRunUseCase: DeleteRunUseCase
): ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var savedRunId: Int? = null

    init {
        val result = resultRepository.get()
        if (result != null) {
            _state.update { ResultContract.State.Success(result) }
            saveRun(result)
        } else {
            _state.update { ResultContract.State.Error("Результат не найден") }
        }
    }

    fun onIntent(intent: ResultContract.Intent) {
        when (intent) {
            ResultContract.Intent.DeleteAndGoHome -> deleteRun(goHome = true)
            ResultContract.Intent.DeleteAndGoBack -> deleteRun(goHome = false)
        }
    }

    private fun saveRun(result: ExperimentResult) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result) }
                .onSuccess { savedRunId = it }
                .onFailure { Log.e("ResultViewModel", "Ошибка сохранения", it) }
        }
    }

    private fun deleteRun(goHome: Boolean) {
        val runId = savedRunId ?: return

        viewModelScope.launch {
            runCatching { deleteRunUseCase(runId) }
                .onSuccess {
                    _effect.send(
                        if (goHome) {
                            ResultContract.Effect.NavigateHome
                        } else {
                            ResultContract.Effect.NavigateBack
                        }
                    )
                }
                .onFailure {
                    Log.e("ResultViewModel", "Ошибка удаления", it)
                }
        }
    }
}