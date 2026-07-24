package com.imglmd.core.ui.haptic

import androidx.compose.runtime.compositionLocalOf

val LocalHapticManager = compositionLocalOf<HapticManager> {
    error("Хаптик Манагера нэт")
}