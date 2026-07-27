package com.imglmd.core.ui.haptic

import androidx.compose.ui.hapticfeedback.HapticFeedbackType

interface HapticManager {
    fun perform(type: HapticFeedbackType)
}

