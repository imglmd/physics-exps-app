package com.imglmd.physicsexps.core.network

data class OnlineState(
    val hasInternet: Boolean = false,
    val serverAvailable: Boolean = false,
    val offlineMode: Boolean = false
) {
    val canUseOnlineFeatures: Boolean
        get() = hasInternet && serverAvailable && !offlineMode
}