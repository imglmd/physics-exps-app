package com.imglmd.physicsexps.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class IconPosition {
    Start,
    End,
    EdgeStart,
    EdgeEnd
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    borderColor: Color? = null,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.Start
) {
    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        enabled = enabled && !isLoading,
        colors = colors,
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(4.dp),
        border = borderColor?.let {
            BorderStroke(2.dp, it)
        }
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {
            this@Button.AnimatedVisibility(visible = !isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    when (iconPosition) {

                        IconPosition.Start, IconPosition.End -> {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (icon != null && iconPosition == IconPosition.Start) {
                                    Icon(icon, null)
                                }

                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (icon != null && iconPosition == IconPosition.End) {
                                    Icon(icon, null)
                                }
                            }
                        }

                        IconPosition.EdgeStart, IconPosition.EdgeEnd -> {

                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            icon?.let {
                                Row(
                                    modifier = Modifier.align(
                                        if (iconPosition == IconPosition.EdgeStart)
                                            Alignment.CenterStart
                                        else
                                            Alignment.CenterEnd
                                    )
                                ) {
                                    if (iconPosition== IconPosition.EdgeStart) Spacer(Modifier.width(8.dp))
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null,

                                    )
                                    if (iconPosition== IconPosition.EdgeEnd) Spacer(Modifier.width(8.dp))

                                }
                            }
                        }
                    }
                }

                this@Button.AnimatedVisibility(visible = isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}