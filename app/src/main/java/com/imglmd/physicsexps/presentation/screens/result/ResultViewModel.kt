package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: InMemoryResultRepository,
    private val saveRunUseCase: SaveRunUseCase
): ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    init {
        val result = resultRepository.get()
        if (result != null) {
            _state.update { ResultContract.State.Success(result) }
            saveRun(result)
        } else {
            _state.update { ResultContract.State.Error("Результат не найден") }
        }
    }

    private fun saveRun(result: ExperimentResult) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result) }
                .onFailure { Log.e("ResultViewModel", "Ошибка сохранения", it) }
        }
    }
}