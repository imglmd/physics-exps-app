package com.imglmd.physicsexps.feature.settings.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.ui.components.RadioOption
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsButton
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsGroup
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsRadioGroup
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsSlider
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsSwitch
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
                        "Настройки",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            item {
                SettingsGroup(
                    title = "Внешний вид"
                ) {
                    SettingsRadioGroup(
                        options = listOf(
                            RadioOption(
                                value = AppTheme.SYSTEM,
                                title = "Системная",
                                subtitle = "Как в настройках устройства"
                            ),
                            RadioOption(
                                value = AppTheme.LIGHT,
                                title = "Светлая"
                            ),
                            RadioOption(
                                value = AppTheme.DARK,
                                title = "Тёмная"
                            )
                        ),
                        selected = state.settings.theme,
                        onSelected = { viewModel.onIntent(SettingsIntent.ThemeChanged(it)) }
                    )
                    if (isDarkTheme) {
                        SettingsSwitch(
                            title = "Экстра тёмная тема",
                            subtitle = "Полностью чёрный интерфейс для OLED и AMOLED экранов",
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
                        SettingsSwitch(
                            title = "Динамические цвета",
                            subtitle = "Подстраивает цвета приложения под обои устройства",
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
                SettingsGroup("Эксперименты") {
                    SettingsSwitch(
                        title = "Показывать все параметры",
                        subtitle = "Дополнительные параметры будут открыты сразу",
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
                SettingsGroup("История") {
                    SettingsSlider(
                        title = "Максимум записей",
                        subtitle = "Старые записи будут удаляться автоматически",
                        value = state.settings.maxHistoryEntries,
                        values = listOf(50, 100, 200, null),
                        valueLabel = { it?.toString() ?: "∞ " },
                        onValueChange = { viewModel.onIntent(SettingsIntent.MaxHistoryChanged(it))}
                    )
                }
            }
            item {
                SettingsGroup("Дополнительные фичи") {
                    SettingsSwitch(
                        title = "Виброотклик",
                        subtitle = "Лёгкая вибрация при нажатии кнопок и переключателей",
                        checked = state.settings.hapticFeedback,
                        onCheckedChange = {
                            viewModel.onIntent(
                                SettingsIntent.HapticFeedbackChanged(
                                    it
                                )
                            )
                        }
                    )
                }
            }
            item {
                SettingsGroup("О приложении") {
                    SettingsButton(
                        title = "GitHub",
                        onClick = {
                            context.openUrl("https://github.com/imglmd/physics-exps-app")
                        },
                        showIcon = true,
                    )
                    SettingsButton(
                        title = "Последний релиз",
                        onClick = {
                            context.openUrl("https://github.com/imglmd/physics-exps-app/releases/latest")
                        },
                        showIcon = true
                    )
                    SettingsButton(
                        title = "Текущая версия: $versionName",
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