package com.imglmd.physicsexps.presentation.screens.result

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.core.OnlineStateManager
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.usecase.comment.AddCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.physicsexps.domain.usecase.media.DeleteMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.UploadMediaUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateBack
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateChart
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateCompare
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateExperiment
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateHome
import com.imglmd.physicsexps.presentation.screens.result.ResultContract.Effect.NavigateSolution
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
    private val appContext: Context,
    private val resultRepository: InMemoryResultRepository,
    private val saveRunUseCase: SaveRunUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    private val getRunUseCase: GetRunUseCase,
    private val getResultUseCase: GetResultUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getMediaUseCase: GetMediaUseCase,
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    private val onlineStateManager: OnlineStateManager
) : ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    private val _effect = Channel<ResultContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val isNewRun: Boolean = runId == null
    private var savedRunId: Int? = runId
    private var savedRemoteRunId: String? = null

    private val json = Json

    init {
        observeOnlineState()
        if (isNewRun) {
            initNewRun()
        } else {
            loadExistingRun(runId!!)
        }
    }

    private fun initNewRun() {
        val bundle = resultRepository.get()
        if (bundle == null) {
            _state.value = ResultContract.State.Error("Результат не найден")
            return
        }

        _state.value = ResultContract.State.Success(
            result = bundle.result,
            onlineState = onlineStateManager.state.value,
            isSaved = false,
            isSaving = true
        )
        saveRun(bundle.result, bundle.inputs, bundle.replaceRunId)
    }

    private fun loadExistingRun(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            runCatching {
                val run = getRunUseCase(id)
                savedRemoteRunId = run.remoteId

                getResultUseCase(id)
            }
                .onSuccess { result ->
                    if (result != null) {
                        _state.value = ResultContract.State.Success(result, onlineState = onlineStateManager.state.value)
                        loadComments()
                        loadMedia()
                    } else {
                        _state.value = ResultContract.State.Error("Результат не найден в базе")
                    }
                }
                .onFailure {
                    _state.value = ResultContract.State.Error("Ошибка загрузки: ${it.message}")
                }
        }
    }

    fun onIntent(intent: ResultContract.Intent) = when (intent) {
        ResultContract.Intent.Back -> handleBack()
        ResultContract.Intent.Save -> emit(NavigateHome)
        ResultContract.Intent.Delete -> deleteRunThen { emit(NavigateHome) }
        ResultContract.Intent.Change -> handleChange()
        ResultContract.Intent.Compare -> emit(NavigateCompare(savedRunId ?: 0))
        ResultContract.Intent.OpenChart -> savedRunId?.let { emit(NavigateChart(it)) }
        ResultContract.Intent.OpenSolution -> emit(NavigateSolution)
        ResultContract.Intent.RefreshMedia -> loadMedia()
        is ResultContract.Intent.AddComment -> addComment(intent.text)
        is ResultContract.Intent.DeleteComment -> deleteComment(intent.id)
        is ResultContract.Intent.UploadMedia -> uploadMedia(intent.uri)
        is ResultContract.Intent.DeleteMedia -> deleteMedia(intent.mediaId)
    }

    private fun handleBack() {
        if (isNewRun) {
            deleteRunThen { emit(NavigateBack) }
        } else {
            emit(NavigateBack)
        }
    }

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

                    val run = getRunUseCase(newId)
                    savedRemoteRunId = run.remoteId

                    if (replaceRunId != null) {
                        //deleteRemoteMediaForRun(replaceRunId)
                        runCatching { deleteRunUseCase(replaceRunId) }
                    }

                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(isSaved = true, isSaving = false)
                        } else {
                            state
                        }
                    }
                    loadComments()
                    loadMedia()
                }
                .onFailure {
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(isSaving = false)
                        } else {
                            state
                        }
                    }
                }
        }
    }

    private fun deleteRunThen(onSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            val id = savedRunId
            if (id != null) {
                // deleteRemoteMediaForRun(id)
                runCatching { deleteRunUseCase(id) }
                    .onSuccess { onSuccess() }
            } else {
                onSuccess()
            }
        }
    }

    private fun observeOnlineState() {
        viewModelScope.launch {
            onlineStateManager.state.collect { onlineState ->
                _state.update { state ->
                    if (state is ResultContract.State.Success) {
                        state.copy(onlineState = onlineState)
                    } else {
                        state
                    }
                }
            }
        }
    }

    private fun loadComments() {
        val id = savedRunId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val comments = getCommentsUseCase(id)
            _state.update { state ->
                if (state is ResultContract.State.Success) {
                    state.copy(comments = comments)
                } else {
                    state
                }
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

    private fun loadMedia(showLoading: Boolean = true) {
        if (!onlineStateManager.state.value.canUseOnlineFeatures) return

        val remoteId = savedRemoteRunId ?: return

        if (showLoading) {
            _state.update { state ->
                if (state is ResultContract.State.Success) {
                    state.copy(
                        isMediaLoading = true,
                        mediaErrorMessage = null
                    )
                } else {
                    state
                }
            }
        }

        viewModelScope.launch {
            getMediaUseCase(remoteId)
                .onSuccess { mediaList ->
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(
                                media = mediaList.media,
                                isMediaLoading = false,
                                mediaErrorMessage = null
                            )
                        } else {
                            state
                        }
                    }
                }
                .onFailure { throwable ->
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(
                                isMediaLoading = false,
                                mediaErrorMessage = throwable.userMessage(
                                    "Не удалось загрузить вложения"
                                )
                            )
                        } else {
                            state
                        }
                    }
                }
        }
    }

    private fun uploadMedia(uri: Uri) {
        if (!onlineStateManager.state.value.canUseOnlineFeatures) return

        val remoteId = savedRemoteRunId ?: return

        _state.update { state ->
            if (state is ResultContract.State.Success) {
                state.copy(
                    isMediaUploading = true,
                    mediaErrorMessage = null
                )
            } else {
                state
            }
        }

        viewModelScope.launch {
            uploadMediaUseCase(appContext, uri, remoteId)
                .onSuccess {
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(isMediaUploading = false)
                        } else {
                            state
                        }
                    }
                    loadMedia(showLoading = false)
                }
                .onFailure { e ->
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(
                                isMediaUploading = false,
                                mediaErrorMessage = e.userMessage(
                                    "Не удалось загрузить файл"
                                )
                            )
                        } else {
                            state
                        }
                    }
                }
        }
    }

    private fun deleteMedia(mediaId: String) {
        if (!onlineStateManager.state.value.canUseOnlineFeatures) return

        val remoteId = savedRemoteRunId ?: return

        viewModelScope.launch {
            deleteMediaUseCase(remoteId, mediaId)
                .onSuccess {
                    loadMedia(showLoading = false)
                }
                .onFailure { throwable ->
                    _state.update { state ->
                        if (state is ResultContract.State.Success) {
                            state.copy(
                                mediaErrorMessage = throwable.userMessage(
                                    "Не удалось удалить файл"
                                )
                            )
                        } else {
                            state
                        }
                    }
                }
        }
    }

    private suspend fun deleteRemoteMediaForRun(runId: Int) {
        val run = getRunUseCase(runId)
        val remoteId = run.remoteId

        val media = getMediaUseCase(remoteId).getOrNull()?.media.orEmpty()

        media.forEach { item -> deleteMediaUseCase(remoteId, item.mediaId) }
    }

    private fun Throwable.userMessage(defaultMessage: String): String {
        val message = message?.trim().orEmpty()
        return if (message.isEmpty()) defaultMessage else message
    }

    private fun emit(effect: ResultContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
