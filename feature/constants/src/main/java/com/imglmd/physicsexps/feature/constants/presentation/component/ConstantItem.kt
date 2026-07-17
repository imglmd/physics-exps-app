package com.imglmd.physicsexps.feature.constants.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.feature.constants.domain.model.Constant
import com.imglmd.physicsexps.feature.constants.presentation.ConstantsPreferences
import com.imglmd.physicsexps.feature.constants.presentation.CopyMode
import com.imglmd.physicsexps.feature.constants.presentation.util.format

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
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
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