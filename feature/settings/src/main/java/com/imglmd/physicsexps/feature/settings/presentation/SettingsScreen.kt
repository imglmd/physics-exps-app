package com.imglmd.physicsexps.feature.settings.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.core.ui.preferences.PreferenceButton
import com.imglmd.physicsexps.core.ui.preferences.PreferenceGroup
import com.imglmd.physicsexps.core.ui.preferences.PreferenceSlider
import com.imglmd.physicsexps.core.ui.preferences.PreferenceSwitch
import com.imglmd.physicsexps.core.ui.RadioGroup
import com.imglmd.physicsexps.core.ui.RadioOption
import com.imglmd.physicsexps.feature.settings.R
import com.imglmd.physicsexps.feature.settings.domain.model.AppLanguage
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    versionName: String,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isDarkTheme = when (state.settings.theme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 110.dp,
                start = 16.dp,
                end = 16.dp
            ),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.settings_s),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            item {
                PreferenceGroup(
                    title = stringResource(R.string.appearance)
                ) {
                    RadioGroup(
                        options = listOf(
                            RadioOption(
                                value = AppTheme.SYSTEM,
                                title = stringResource(R.string.systemic),
                                subtitle = stringResource(R.string.device_set)
                            ),
                            RadioOption(
                                value = AppTheme.LIGHT,
                                title = stringResource(R.string.light)
                            ),
                            RadioOption(
                                value = AppTheme.DARK,
                                title = stringResource(R.string.dark)
                            )
                        ),
                        selected = state.settings.theme,
                        onSelected = { viewModel.onIntent(SettingsIntent.ThemeChanged(it)) }
                    )
                    if (isDarkTheme) {
                        PreferenceSwitch(
                            title = stringResource(R.string.extra_dark),
                            subtitle = stringResource(R.string.dark_desc),
                            checked = state.settings.amoledTheme,
                            onCheckedChange = {
                                viewModel.onIntent(
                                    SettingsIntent.AmoledThemeChanged(
                                        it
                                    )
                                )
                            }
                        )
                    }
                    if (!isDarkTheme){
                        PreferenceSwitch(
                            title = stringResource(R.string.dynamic_colors),
                            subtitle = stringResource(R.string.adjusts),
                            checked = state.settings.dynamicColors,
                            onCheckedChange = {
                                viewModel.onIntent(
                                    SettingsIntent.DynamicColorsChanged(it)
                                )
                            }
                        )
                    }
                }
            }
            item {
                PreferenceGroup(stringResource(R.string.history_s)) {
                    PreferenceSlider(
                        title = stringResource(R.string.max_rec),
                        subtitle = stringResource(R.string.old_rec),
                        value = state.settings.maxHistoryEntries,
                        values = listOf(50, 100, 200, null),
                        valueLabel = { it?.toString() ?: "∞ " },
                        onValueChange = { viewModel.onIntent(SettingsIntent.MaxHistoryChanged(it))}
                    )
                }
            }

            item {
                PreferenceGroup(stringResource(R.string.exps)) {
                    PreferenceSwitch(
                        title = stringResource(R.string.show_all_p),
                        subtitle = stringResource(R.string.add_params_desc),
                        checked = state.settings.advancedMode,
                        onCheckedChange = {
                            viewModel.onIntent(
                                SettingsIntent.AdvancedModeChanged(
                                    it
                                )
                            )
                        }
                    )
                }
            }

            item {
                PreferenceGroup(stringResource(R.string.language)){
                    RadioGroup(
                        options = listOf(
                            RadioOption(
                                value = AppLanguage.RUSSIAN,
                                title = "Русский"
                            ),
                            RadioOption(
                                value = AppLanguage.ENGLISH,
                                title = "English"
                            )
                        ),
                        selected = state.settings.language,
                        onSelected = { viewModel.onIntent(SettingsIntent.ChangeLanguage(it)) }
                    )
                }
            }
            item {
                PreferenceGroup(stringResource(R.string.additional_features)) {
                    PreferenceSwitch(
                        title = stringResource(R.string.haptic_feedback),
                        subtitle = stringResource(R.string.haptic_desc),
                        checked = state.settings.hapticFeedback,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.HapticFeedbackChanged(it)) }
                    )
                    PreferenceSwitch(
                        title = stringResource(R.string.offline),
                        subtitle = stringResource(R.string.dis_online),
                        checked = state.settings.offlineMode,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.OfflineModeChanged(it))}
                    )
                }
            }
            item {
                PreferenceGroup(stringResource(R.string.about_app)) {
                    PreferenceButton(
                        title = "GitHub",
                        onClick = {
                            context.openUrl("https://github.com/imglmd/physics-exps-app")
                        },
                        showIcon = true,
                    )
                    PreferenceButton(
                        title = stringResource(R.string.latest),
                        onClick = {
                            context.openUrl("https://github.com/imglmd/physics-exps-app/releases/latest")
                        },
                        showIcon = true
                    )
                    PreferenceButton(
                        title = stringResource(R.string.current_ver) + versionName,
                        onClick = null,
                        showIcon = false,
                    )
                }
            }
        }
    }
}

private fun Context.openUrl(url: String) {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                url.toUri()
            )
        )
    }
}