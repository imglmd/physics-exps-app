package com.imglmd.physicsexps.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.core.ui.theme.PhysicsExpsTheme
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository
import com.imglmd.physicsexps.presentation.navigation.AppRoot
import org.koin.android.ext.android.inject
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {
    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { keepSplash }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepo: SettingsRepository by inject()
            val settings by settingsRepo.settings.collectAsStateWithLifecycle(null)

            LaunchedEffect(settings) {
                if (settings != null) keepSplash = false
            }

            val darkTheme = when (settings?.theme) {
                AppTheme.DARK   -> true
                AppTheme.LIGHT  -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
                null -> true
            }
            settings?.let { currentSettings ->
                PhysicsExpsTheme(
                    darkTheme = darkTheme,
                    amoledTheme = currentSettings.amoledTheme,
                    dynamicColor = currentSettings.dynamicColors
                ) {
                    AppRoot(currentSettings)
                }
            }
        }
    }
}