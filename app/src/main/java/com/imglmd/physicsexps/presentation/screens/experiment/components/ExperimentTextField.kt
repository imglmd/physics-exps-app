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
import androidx.compose.ui.graphics.Color
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
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant
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
                color = borderColor,
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
                .background(borderColor)
        )

        BasicTextField(
            state = state,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            textStyle = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 12.dp),
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
                            if (hasDecimal) {
                                delete(i, i + 1)
                            } else {
                                hasDecimal = true
                            }
                        }

                        c == '-' -> {
                            if (i != 0 || hasMinus) {
                                delete(i, i + 1)
                            } else {
                                hasMinus = true
                            }
                        }

                        else -> delete(i, i + 1)
                    }
                }

                if (length == 1 && charAt(0) == '.') {
                    insert(0, "0")
                }

                if (length >= 2 && charAt(0) == '-' && charAt(1) == '.') {
                    insert(1, "0")
                }
            },
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