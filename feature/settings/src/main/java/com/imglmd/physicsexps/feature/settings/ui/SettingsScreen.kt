package com.imglmd.physicsexps.feature.settings.ui

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.ui.components.RadioOption
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsButton
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsGroup
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsRadioGroup
import com.imglmd.physicsexps.feature.settings.ui.components.SettingsSwitch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Настройки",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(20.dp))
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
                        onSelected = {
                            viewModel.onIntent(
                                SettingsIntent.ThemeChanged(it)
                            )
                        }
                    )
                }
            }
            item {
                SettingsGroup("О приложении") {
                    SettingsButton(
                        title = "Версия 1.6.7", // TODO: поменять на норм версию
                        onClick = {
                              context.openUrl("https://github.com/imglmd/physics-exps-app")
                        },
                        showIcon = true,
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