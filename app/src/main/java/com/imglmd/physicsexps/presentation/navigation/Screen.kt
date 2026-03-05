package com.imglmd.physicsexps.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen
    @Serializable
    data class Experiment(val id: Int) : Screen
}