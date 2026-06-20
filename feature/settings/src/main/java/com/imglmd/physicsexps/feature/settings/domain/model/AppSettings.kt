package com.imglmd.physicsexps.feature.settings.domain.model

enum class AppTheme { LIGHT, DARK, SYSTEM }


data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM
)