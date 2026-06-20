@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.imglmd.physicsexps.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.imglmd.physicsexps.domain.usecase.auth.AuthState

@Composable
fun AuthIndicator(
    state: AuthState,
    modifier: Modifier = Modifier
) {

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

        when (state) {

            AuthState.Authorizing -> {

                LoadingIndicator(
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AuthState.Authorized -> {

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cloud_done),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
            }

            AuthState.Offline -> {

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cloud_off),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
            }

            AuthState.Idle -> {

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cloud),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
            }
        }
    }
}