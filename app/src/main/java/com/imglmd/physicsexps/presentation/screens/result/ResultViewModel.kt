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
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
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
    private val getResultUseCase: GetResultUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val isNewRun: Boolean = runId == null
    private var savedRunId: Int? = runId

    private val json = Json

    init {
        if (isNewRun) initNewRun()
        else loadExistingRun(runId!!)
    }

    // инициализация
    private fun initNewRun() {
        val bundle = resultRepository.get()
        if (bundle == null) {
            _state.value = ResultContract.State.Error("Результат не найден")
            return
        }
        _state.value = ResultContract.State.Success(
            result = bundle.result,
            isSaved = false,
            isSaving = true
        )
        saveRun(bundle.result, bundle.inputs, bundle.replaceRunId)
    }

    private fun loadExistingRun(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { getResultUseCase(id) }
                .onSuccess { result ->
                    if (result != null) {
                        _state.value = ResultContract.State.Success(result)
                        loadComments()
                    } else {
                        _state.value = ResultContract.State.Error("Результат не найден в базе")
                    }
                }
                .onFailure {
                    _state.value = ResultContract.State.Error("Ошибка загрузки: ${it.message}")
                }
        }
    }

    // интенты
    fun onIntent(intent: ResultContract.Intent) = when (intent) {
        ResultContract.Intent.Back -> handleBack()
        ResultContract.Intent.Save -> emit(NavigateHome)
        ResultContract.Intent.Delete -> deleteRunThen { emit(NavigateHome) }
        ResultContract.Intent.Change -> handleChange()

        ResultContract.Intent.OpenChart -> savedRunId?.let { emit(NavigateChart(savedRunId!!)) }
        ResultContract.Intent.OpenSolution -> emit(NavigateSolution)
        is ResultContract.Intent.AddComment -> addComment(intent.text)
        is ResultContract.Intent.DeleteComment -> deleteComment(intent.id)
        ResultContract.Intent.Compare -> emit(NavigateCompare(savedRunId?: 0))
    }


    private fun handleBack() {
        if (isNewRun) {
            deleteRunThen { emit(NavigateBack) }
        } else {
            emit(NavigateBack)
        }
    }


    // change: передаём inputs + replaceRunId на экран эксперимента
    // старый run удаляется только после успешного сохранения нового (в следующем initNewRun)
    private fun handleChange() {
        val id = savedRunId ?: return
        viewModelScope.launch {
            runCatching { getRunUseCase(id) }
                .onSuccess { run ->
                    val inputs: Map<String, Double> = json.decodeFromString(run.inputData)
                    emit(
                        NavigateExperiment(
                            id = run.experimentId,
                            inputs = inputs.mapValues { it.value.toString() },
                            replaceRunId = id
                        )
                    )
                }
                .onFailure { Log.e(TAG, "Ошибка получения run для Change", it) }
        }
    }

    private fun saveRun(
        result: ExperimentResult,
        inputs: Map<String, Double>,
        replaceRunId: Int?
    ) {
        viewModelScope.launch {
            runCatching { saveRunUseCase(result, inputs) }
                .onSuccess { newId ->
                    savedRunId = newId

                    // удаление старого run только после успешного сохранения нового
                    if (replaceRunId != null) {
                        runCatching { deleteRunUseCase(replaceRunId) }
                            .onFailure { Log.e(TAG, "Ошибка удаления заменяемого run", it) }
                    }

                    _state.update { s ->
                        if (s is ResultContract.State.Success)
                            s.copy(isSaved = true, isSaving = false)
                        else s
                    }
                    loadComments()
                }
                .onFailure {
                    Log.e(TAG, "Ошибка сохранения", it)
                    _state.update { s ->
                        if (s is ResultContract.State.Success) s.copy(isSaving = false) else s
                    }
                }
        }
    }

    private fun deleteRunThen(onSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            val id = savedRunId
            if (id != null) {
                runCatching { deleteRunUseCase(id) }
                    .onSuccess { onSuccess() }
                    .onFailure { Log.e(TAG, "Ошибка удаления run", it) }
            } else {
                // run ещё не сохранен или уже удален
                onSuccess()
            }
        }
    }

    // комментарии
    private fun loadComments() {
        val id = savedRunId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val comments = getCommentsUseCase(id)
            _state.update { s ->
                if (s is ResultContract.State.Success) s.copy(comments = comments) else s
            }
        }
    }

    private fun addComment(text: String) {
        val id = savedRunId ?: return
        viewModelScope.launch {
            addCommentUseCase(Comment(experimentRunId = id, text = text))
            loadComments()
        }
    }

    private fun deleteComment(id: Int) {
        viewModelScope.launch {
            deleteCommentUseCase(id)
            loadComments()
        }
    }



    private fun emit(effect: ResultContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    companion object {
        private const val TAG = "ResultViewModel"
    }
}