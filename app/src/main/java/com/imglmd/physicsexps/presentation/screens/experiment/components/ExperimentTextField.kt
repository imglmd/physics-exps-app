package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExperimentTextField(
    state: TextFieldState,
    label: String,
    symbol: String,
    unit: String,
    modifier: Modifier = Modifier,
    symbolWidth: Dp = 44.dp,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(symbolWidth)
                .fillMaxHeight()
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.5.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )

        BasicTextField(
            state = state,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = { innerTextField ->
                if (state.text.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                }
                innerTextField()

            }
        )

        Text(
            text = unit,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 14.dp, start = 4.dp)
        )
    }
}