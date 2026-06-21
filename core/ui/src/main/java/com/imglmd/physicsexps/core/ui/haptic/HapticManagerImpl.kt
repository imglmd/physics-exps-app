package com.imglmd.physicsexps.core.ui.haptic

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

class HapticManagerImpl(
    private val haptic: HapticFeedback,
    private val isEnabled: Boolean
): HapticManager {

    override fun perform(type: HapticFeedbackType) {
        if (isEnabled) {
            haptic.performHapticFeedback(type)
        }
    }
}