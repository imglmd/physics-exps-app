package com.imglmd.physicsexps.core

import com.imglmd.physicsexps.core.network.NetworkMonitor
import com.imglmd.physicsexps.core.network.OfflineModeProvider
import com.imglmd.physicsexps.core.network.OnlineState
import com.imglmd.physicsexps.domain.usecase.auth.PingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class OnlineStateManager(
    networkMonitor: NetworkMonitor,
    offlineModeProvider: OfflineModeProvider,
    private val pingUseCase: PingUseCase
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val serverAvailable = MutableStateFlow(false)

    init {
        scope.launch {
            networkMonitor.isConnected.collectLatest { connected ->

                if (!connected) {
                    serverAvailable.value = false
                    return@collectLatest
                }

                while (true) {
                    serverAvailable.value =
                        runCatching {
                            pingUseCase()
                            true
                        }.getOrDefault(false)

                    delay(30.seconds)
                }
            }
        }
    }

    val state: StateFlow<OnlineState> =
        combine(
            networkMonitor.isConnected,
            serverAvailable,
            offlineModeProvider.offlineMode
        ) { internet, server, offline ->

            OnlineState(
                hasInternet = internet,
                serverAvailable = server,
                offlineMode = offline
            )
        }.stateIn(scope, SharingStarted.Eagerly, OnlineState())
}