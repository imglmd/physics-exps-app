package com.imglmd.physicsexps.presentation.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Navigator(startDestination: Screen) {
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(startDestination)

    private val _currentTab = MutableStateFlow(Screen.Tab.Home)
    val currentTab = _currentTab.asStateFlow()
    private val tabHistory = ArrayDeque<Screen.Tab>().also { it.addLast(Screen.Tab.Home) }

    fun switchTab(tab: Screen.Tab) {
        if (_currentTab.value == tab) return
        tabHistory.addLast(tab)
        _currentTab.value = tab
    }

    fun popTab(): Boolean {
        if (tabHistory.size <= 1) return false
        tabHistory.removeLast()
        _currentTab.value = tabHistory.last()
        return true
    }

    fun navigateTo(destination: Screen) {
        backStack.add(destination)
    }

    fun replaceTo(destination: Screen) {
        if (backStack.isEmpty()) return

        backStack.removeLastOrNull()

        if (backStack.lastOrNull() is Screen.Experiment) {
            backStack.removeLastOrNull()
        }

        backStack.add(destination)
    }
    fun goHome() {
        if (backStack.size > 1) backStack.subList(1, backStack.size).clear()
        tabHistory.clear()
        tabHistory.addLast(Screen.Tab.Home)
        _currentTab.value = Screen.Tab.Home
    }
    fun goBack() {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }
}