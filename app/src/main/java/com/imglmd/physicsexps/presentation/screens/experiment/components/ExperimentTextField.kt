package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
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
    isError: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    val borderColor = when {
        isError -> colors.error
        else -> colors.outlineVariant
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .heightIn(min = 60.dp)
            .background(
                color = colors.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(symbolWidth)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.titleLarge,
                color = colors.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .height(32.dp)
                .width(1.dp)
                .background(colors.outlineVariant.copy(alpha = 0.5f))
        )

        BasicTextField(
            state = state,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = colors.onSurface
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            lineLimits = TextFieldLineLimits.SingleLine,

            inputTransformation = InputTransformation {

                for (i in 0 until length) {
                    if (charAt(i) == ',') {
                        delete(i, i + 1)
                        insert(i, ".")
                    }
                }

                var hasDecimal = false
                var hasMinus = false

                for (i in 0 until length) {
                    val c = charAt(i)

                    when {
                        c.isDigit() -> Unit

                        c == '.' -> {
                            if (hasDecimal) delete(i, i + 1)
                            else hasDecimal = true
                        }

                        c == '-' -> {
                            if (i != 0 || hasMinus) delete(i, i + 1)
                            else hasMinus = true
                        }

                        else -> delete(i, i + 1)
                    }
                }

                if (length == 1 && charAt(0) == '.') insert(0, "0")
                if (length >= 2 && charAt(0) == '-' && charAt(1) == '.') insert(1, "0")
            },

            cursorBrush = SolidColor(colors.primary),

            decorator = { innerTextField ->

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.onSurfaceVariant
                        )
                    }

                    innerTextField()
                }
            }
        )

        Text(
            text = unit,
            style = MaterialTheme.typography.titleMedium,
            color = colors.onSurfaceVariant,
            modifier = Modifier.padding(end = 12.dp, start = 4.dp)
        )
    }
}