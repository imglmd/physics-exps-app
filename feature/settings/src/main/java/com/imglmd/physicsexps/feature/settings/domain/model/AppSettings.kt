package com.imglmd.physicsexps.feature.settings.domain.model

enum class AppTheme { LIGHT, DARK, SYSTEM }
enum class AppLanguage { RUSSIAN, ENGLISH }

data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val amoledTheme: Boolean = false,
    val dynamicColors: Boolean = false,
    val language: AppLanguage = AppLanguage.RUSSIAN,
    val hapticFeedback: Boolean = true,

    val offlineMode: Boolean = false,
    val advancedMode: Boolean = false,
    val maxHistoryEntries: Int? = null
)