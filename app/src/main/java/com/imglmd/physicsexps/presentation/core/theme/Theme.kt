package com.imglmd.physicsexps.presentation.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CherryRose,
    secondary = Silver,
    tertiary = DimSilver,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    background = NightBlack,
    surfaceContainer = DarkSurface,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = Parchment,
    onPrimary = White,
    onBackground = Parchment,
    onSurface = Parchment,
    onSurfaceVariant = Silver,
    error = CherryRose,
    outline = DimSilver,
    outlineVariant = DimSilver,
    onSecondary = White,
    scrim = NightBlack
)
private val LightColorScheme = lightColorScheme(
    primary = CherryRose,
    secondary = Silver,
    tertiary = AlabasterGrey,
    surface = White,
    surfaceVariant = White,
    background = White,
    surfaceContainer = White,
    primaryContainer = White,
    onPrimaryContainer = ShadowGrey,
    onPrimary = White,
    onBackground = ShadowGrey,
    onSurface = ShadowGrey,
    onSurfaceVariant = Silver,
    error = CherryRose,
    outline = AlabasterGrey,
    outlineVariant = AlabasterGrey,
    onSecondary = White,
    scrim = ShadowGrey
)

@Composable
fun PhysicsExpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}