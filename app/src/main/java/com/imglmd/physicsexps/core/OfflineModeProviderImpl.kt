package com.imglmd.physicsexps.core

import com.imglmd.core.network.OfflineModeProvider
import com.imglmd.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.map

class OfflineModeProviderImpl(
    settingsRepository: SettingsRepository
): OfflineModeProvider {

    override val offlineMode = settingsRepository.settings.map { it.offlineMode }
}