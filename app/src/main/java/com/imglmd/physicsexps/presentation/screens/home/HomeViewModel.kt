package com.imglmd.physicsexps.presentation.screens.home

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.core.OnlineStateManager
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.usecase.auth.AuthState
import com.imglmd.physicsexps.experiments.ExperimentRegistry
import com.imglmd.physicsexps.domain.usecase.auth.EnsureAuthorizedUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetLastRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeViewModel(
    application: Application, // для контекста
    getAllExperimentsUseCase: GetAllExperimentsUseCase,
    private val getLastRunsUseCase: GetLastRunsUseCase,
    private val getResultUseCase: GetResultUseCase,
    private val registry: ExperimentRegistry,
    private val getRunUseCase: GetRunUseCase,
    private val resultRepository: InMemoryResultRepository,
    //private val getExperimentPreviewsUseCase: GetExperimentPreviewsUseCase,
    private val ensureAuthorizedUseCase: EnsureAuthorizedUseCase,
    private val onlineStateManager: OnlineStateManager
): AndroidViewModel(application) {

    private val allExperiments = getAllExperimentsUseCase()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<HomeAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    //private var previewsLoaded = false
    private val json = Json

    init {
        observeOnlineState()
        onInit()
    }
    private fun onInit(){
        viewModelScope.launch {
            updateExperiments("")
            loadHistory()
        }
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ChangeSearchText -> updateExperiments(intent.text)
            is HomeIntent.NavigateToRunResult -> navigateToResult(intent.id)
            is HomeIntent.NavigateToExperiment -> navigateToExperiment(intent.id)
            HomeIntent.NavigateToHistory -> navigateToHistory()
        }
    }

    private fun observeOnlineState() {
        viewModelScope.launch {
            onlineStateManager.state.collect { onlineState ->
                _state.update { it.copy(onlineState = onlineState) }
                if (onlineState.canUseOnlineFeatures) ensureAuthorization()
                /*if (onlineState.canUseOnlineFeatures && !previewsLoaded) {
                    previewsLoaded = true

                    val authState = ensureAuthorization()
                    if (authState == AuthState.Authorized) loadPreviewImages()
                }*/
            }
        }
    }

    private fun updateExperiments(search: String) {
        _state.update {
            it.copy(
                searchText = search,
                experimentsByCategory = allExperiments.groupBy { exp -> exp.category }
            )
        }
    }
    private fun navigateToExperiment(id: String){
        viewModelScope.launch {
            _actionFlow.emit(HomeAction.NavigateToExperiment(id))
        }
    }
    private fun navigateToResult(id: Int){
        viewModelScope.launch {

            val run = getRunUseCase(id)

            val inputs: Map<String, Double> =
                runCatching {
                    json.decodeFromString<Map<String, Double>>(run.inputData)
                }.getOrDefault(emptyMap())

            val result = getResultUseCase(id) ?: return@launch

            resultRepository.save(result, inputs)
            _actionFlow.emit(HomeAction.NavigateToResult(id))
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getLastRunsUseCase(HISTORY_LIMIT + 1)
                .flowOn(Dispatchers.IO)
                .collectLatest { runs ->
                    val hasMore = runs.size > HISTORY_LIMIT
                    val visibleRuns = runs.take(HISTORY_LIMIT)

                    val historyUi = visibleRuns.map { run ->
                        val inputs: Map<String, Double> =
                            runCatching {
                                json.decodeFromString<Map<String, Double>>(run.inputData)
                            }.getOrDefault(emptyMap())

                        val experiment = runCatching {
                            registry.getById(run.experimentId)
                        }.getOrNull()

                        HistoryItemUi(
                            id = run.id,
                            experimentId = experiment?.id ?: "unknown",
                            experimentName = experiment?.id ?: run.experimentId,
                            category = experiment?.category ?: "",
                            date = run.date,
                            inputs = inputs
                        )
                    }

                    _state.update {
                        it.copy(
                            history = historyUi,
                            hasMoreHistory = hasMore,
                            isHistoryLoaded = true
                        )
                    }
                }
        }
    }

    /*private fun loadPreviewImages() {
        viewModelScope.launch {
            getExperimentPreviewsUseCase()
                .onSuccess { previews ->
                    _state.update { it.copy(previewUrlsByExperimentId = previews) }
                }
        }
    }*/

    @SuppressLint("HardwareIds")
    private suspend fun ensureAuthorization(): AuthState {

        return ensureAuthorizedUseCase(
            deviceId = Settings.Secure.getString(
                getApplication<Application>().contentResolver,
                Settings.Secure.ANDROID_ID
            ),
            deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
        )
    }

    private fun navigateToHistory() {
        viewModelScope.launch {
            _actionFlow.emit(HomeAction.NavigateToHistory)
        }
    }

    private companion object {
        const val HISTORY_LIMIT = 6
    }

}
