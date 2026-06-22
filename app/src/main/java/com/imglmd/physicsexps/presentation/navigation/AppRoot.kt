@file:OptIn(KoinExperimentalAPI::class)

package com.imglmd.physicsexps.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.imglmd.physicsexps.core.ui.haptic.HapticManagerImpl
import com.imglmd.physicsexps.core.ui.haptic.LocalHapticManager
import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun AppRoot(
    settings: AppSettings
) {
    val navigator = koinInject<Navigator>()
    val entryProvider = koinEntryProvider<Any>()

    CompositionLocalProvider(
        LocalHapticManager provides HapticManagerImpl(
            haptic = LocalHapticFeedback.current,
            isEnabled = settings.hapticFeedback
        )
    ) {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = navigator.backStack,
            onBack = {
                val onTabHost = navigator.backStack.lastOrNull() is Screen.TabHost
                if (onTabHost && navigator.popTab()) {

                } else {
                    navigator.goBack()
                }
            },
            entryProvider = entryProvider,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            )
        )
    }
}