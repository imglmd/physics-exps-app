package com.imglmd.physicsexps.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object TabHost : Screen

    @Serializable
    enum class Tab {
        Home, Settings;
        val label: String get() = when (this) {
            Home -> "main"
            Settings -> "settings"
        }
        val selectedIcon: ImageVector get() = when (this) {
            Home -> Icons.Filled.Home
            Settings -> Icons.Filled.Settings
        }
        val unselectedIcon: ImageVector get() = when (this) {
            Home -> Icons.Outlined.Home
            Settings -> Icons.Outlined.Settings
        }
    }

    @Serializable
    data class Experiment(
        val id: String,
        val inputs: Map<String, String>? = null,
        val replaceRunId: Int? = null
    ) : Screen
    @Serializable
    data class Result(val runId: Int? = null) : Screen
    @Serializable
    data class History(
        val mode: HistoryMode = HistoryMode.NORMAL,
        val preselectedIds: List<Int> = emptyList()
    ) : Screen
    @Serializable
    data class FullScreenChart(
        val runId: Int
    ) : Screen
    @Serializable
    data object Solution : Screen

    @Serializable
    data class Compare(val runIds: List<Int>) : Screen
}


@Serializable
enum class HistoryMode {
    NORMAL,
    SELECTION
}
