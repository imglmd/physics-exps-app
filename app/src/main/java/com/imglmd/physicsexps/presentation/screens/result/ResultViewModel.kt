package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: InMemoryResultRepository
): ViewModel() {

    private val _state = MutableStateFlow<ResultState>(ResultState.Loading)
    val state = _state.asStateFlow()

    init {
        val result = resultRepository.get()
        _state.update {
            if (result != null) {
                ResultState.Success(result)
            } else {
                ResultState.Error("Результат не найден")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}