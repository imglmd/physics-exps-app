package com.imglmd.physicsexps.feature.settings.domain.model

enum class AppTheme { LIGHT, DARK, SYSTEM }


data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val amoledTheme: Boolean = false,
    val dynamicColors: Boolean = false,
    val hapticFeedback: Boolean = true,

    val advancedMode: Boolean = false,
    val maxHistoryEntries: Int? = null
)