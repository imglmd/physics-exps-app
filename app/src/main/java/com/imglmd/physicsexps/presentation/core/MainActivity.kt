package com.imglmd.physicsexps.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository
import com.imglmd.physicsexps.presentation.core.theme.PhysicsExpsTheme
import com.imglmd.physicsexps.presentation.navigation.AppRoot
import com.imglmd.physicsexps.presentation.navigation.Navigator
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepo: SettingsRepository by inject()
            val settings by settingsRepo.settings.collectAsStateWithLifecycle(AppSettings())

            val darkTheme = when (settings.theme) {
                AppTheme.DARK   -> true
                AppTheme.LIGHT  -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }
            PhysicsExpsTheme(darkTheme = darkTheme) {
                AppRoot()
            }
        }
    }
}