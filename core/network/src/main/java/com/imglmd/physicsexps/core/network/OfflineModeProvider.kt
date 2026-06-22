package com.imglmd.physicsexps.core.network

import kotlinx.coroutines.flow.Flow

interface OfflineModeProvider {
    val offlineMode: Flow<Boolean>
}