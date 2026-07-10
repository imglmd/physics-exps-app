@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.imglmd.physicsexps.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.core.network.OnlineState

data class ConnectionState(
    val iconRes: Int,
    val label: String,
    val description: String,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
)

fun OnlineState.toConnectionState(): ConnectionState =
    when {
        offlineMode -> ConnectionState(
            iconRes = R.drawable.plane,
            label = "Оффлайн режим",
            description = "Сетевые функции отключены"
        )

        !hasInternet -> ConnectionState(
            iconRes = R.drawable.wifi_off,
            label = "Нет интернета",
            description = "Проверьте подключение к сети"
        )

        !serverAvailable -> ConnectionState(
            iconRes = R.drawable.cloud_off,
            label = "Сервер недоступен",
            description = "Попробуйте позже"
        )

        canUseOnlineFeatures -> ConnectionState(
            iconRes = R.drawable.cloud_done,
            label = "Онлайн",
            description = "Все сетевые функции доступны",
            isOnline = true
        )

        else -> ConnectionState(
            iconRes = R.drawable.cloud,
            label = "Проверка",
            description = "Проверяем соединение",
            isLoading = true
        )
    }
//TODO: сделать кликабельнымп
@Composable
fun ConnectionIndicator(
    state: OnlineState,
    modifier: Modifier = Modifier
) {
    val connectionState = state.toConnectionState()
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {

        if (connectionState.isLoading) {
            LoadingIndicator()
        } else {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(connectionState.iconRes),
                contentDescription = connectionState.label,
                tint = if (connectionState.isOnline)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}