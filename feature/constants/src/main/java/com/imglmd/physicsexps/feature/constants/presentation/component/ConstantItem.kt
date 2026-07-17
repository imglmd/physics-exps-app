package com.imglmd.physicsexps.feature.constants.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Constant
import com.imglmd.physicsexps.feature.constants.presentation.ConstantsPreferences
import com.imglmd.physicsexps.feature.constants.presentation.CopyMode
import com.imglmd.physicsexps.feature.constants.presentation.util.format
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun ConstantItem(
    constant: Constant,
    preferences: ConstantsPreferences,
    modifier: Modifier = Modifier,
    onCopied: (String) -> Unit = {}
) {
    val clipboard = LocalClipboardManager.current
    val unitText = constant.unitRes?.let { stringResource(it) }.orEmpty()
    val nameText = stringResource(constant.nameRes)
    val formattedValue = constant.value.format(preferences.digits)

    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(1.seconds)
            copied = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                val copyText = buildCopyText(
                    constant = constant,
                    nameText = nameText,
                    formattedValue = formattedValue,
                    unitText = unitText,
                    copyMode = preferences.copyMode
                )
                clipboard.setText(AnnotatedString(copyText))
                onCopied(copyText)
                copied = true
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(constant.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (unitText.isBlank()) {
                        "${constant.symbol} = $formattedValue"
                    } else {
                        "${constant.symbol} = $formattedValue $unitText"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.width(6.dp))
            AnimatedContent(
                targetState = copied,
                label = "copy_icon"
            ) { copied ->
                Icon(
                    imageVector = if (copied) Icons.Outlined.CheckCircle else ImageVector.vectorResource(
                        R.drawable.copy
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun buildCopyText(
    constant: Constant,
    nameText: String,
    formattedValue: String,
    unitText: String,
    copyMode: CopyMode
): String = when (copyMode) {
    CopyMode.VALUE -> formattedValue
    CopyMode.SYMBOL_VALUE -> "${constant.symbol} = $formattedValue"
    CopyMode.FULL -> buildString {
        append(nameText)
        append("\n")
        append(constant.symbol)
        append(" = ")
        append(formattedValue)
        if (unitText.isNotBlank()) {
            append(" ")
            append(unitText)
        }
    }
}