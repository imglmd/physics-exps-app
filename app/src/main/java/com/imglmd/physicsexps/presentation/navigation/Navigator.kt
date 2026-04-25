package com.imglmd.physicsexps.presentation.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator(startDestination: Screen) {
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(startDestination)

    fun navigateTo(destination: Screen) {
        backStack.add(destination)
    }

    fun goHome() {
        if (backStack.size > 1) {
            backStack.subList(1, backStack.size).clear()
        }
    }
    fun goBack() {
        backStack.removeLastOrNull()
    }
}