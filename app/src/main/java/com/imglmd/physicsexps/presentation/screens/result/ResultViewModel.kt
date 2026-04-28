package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.usecase.comment.AddCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ResultViewModel(
    private val runId: Int?,
    private val resultRepository: InMemoryResultRepository,
    private val saveRunUseCase: SaveRunUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    private val getRunUseCase: GetRunUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var savedRunId: Int? = runId

    private val json = Json

    init {
        val bundle = resultRepository.get()

        if (bundle != null) {
            _state.value = ResultContract.State.Success(bundle.result)

            loadComments()

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
            ResultContract.Intent.Save -> handleSave()
            ResultContract.Intent.Change -> handleChange()
            is ResultContract.Intent.AddComment -> addComment(intent.text)
            is ResultContract.Intent.DeleteComment -> deleteComment(intent.id)
            ResultContract.Intent.OpenChart -> {
                viewModelScope.launch {
                    val id = savedRunId ?: return@launch
                    _effect.send(NavigateChart(id))
                }
            }

            ResultContract.Intent.OpenSolution -> { viewModelScope.launch { _effect.send(NavigateSolution) } }
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
    private fun handleSave() {
        viewModelScope.launch {
            _effect.send(ResultContract.Effect.NavigateHome)
        }
    }

    private fun loadComments(){
        viewModelScope.launch(Dispatchers.IO) {
            val runId = savedRunId ?: return@launch
            val comments = getCommentsUseCase(runId)
            _state.update { state ->
                if (state is ResultContract.State.Success) {
                    state.copy(comments = comments)
                } else state
            }
        }
    }

    private fun addComment(text: String){
        viewModelScope.launch {
            val runId = savedRunId ?: return@launch
            addCommentUseCase(
                Comment(
                    experimentRunId = runId,
                    text = text,
                )
            )
            loadComments()
        }
    }

    private fun deleteComment(id: Int){
        viewModelScope.launch {
            deleteCommentUseCase(id)
            loadComments()
        }
    }

    private fun saveRun(
        result: ExperimentResult,
        inputs: Map<String, Double>
    ) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result, inputs) }
                .onSuccess {
                    savedRunId = it
                    loadComments()
                }
                .onFailure { Log.e("ResultViewModel", "Ошибка сохранения", it) }
        }
    }

    private suspend fun deleteRunInternal(onSuccess: suspend () -> Unit) {
        val id = savedRunId ?: return
        runCatching { deleteRunUseCase(id) }
            .onSuccess { onSuccess() }
            .onFailure { Log.e("ResultViewModel", "Ошибка удаления", it) }
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
                        val inputs: Map<String, Double> =
                            json.decodeFromString(run.inputData)

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

}