package com.imglmd.physicsexps.presentation.screens.solution

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.LatexAutoWrap
import com.hrm.latex.renderer.model.LatexConfig
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.SolutionStep

@Composable
fun SolutionStepCard(
    index: Int,
    step: SolutionStep,
    focusState: StepFocusState,
    isLast: Boolean
) {
    val contentAlpha by animateFloatAsState(
        targetValue = when {
            !focusState.hasFocus -> 1f
            focusState.isActive -> 1f
            focusState.isPast   -> 0.45f
            else                -> 0.35f
        },
        animationSpec = tween(350),
        label = "step_alpha"
    )

    val borderAlpha by animateFloatAsState(
        targetValue = if (focusState.isActive) 1f else 0f,
        animationSpec = tween(350),
        label = "border_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (focusState.isActive) 1f else 0.97f,
        animationSpec = tween(350),
        label = "step_scale"
    )

    val lineAlpha by animateFloatAsState(
        targetValue = if (focusState.isPast) 0.8f else 0.2f,
        animationSpec = tween(350),
        label = "line_alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = contentAlpha
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0.5f, 0f)
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp, top = 16.dp)
        ) {
            val circleColor = when {
                focusState.isActive -> MaterialTheme.colorScheme.primary
                focusState.isPast   -> MaterialTheme.colorScheme.outline
                else                -> MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = when {
                focusState.isActive -> MaterialTheme.colorScheme.onPrimary
                focusState.isPast   -> MaterialTheme.colorScheme.onSurfaceVariant
                else                -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Surface(shape = CircleShape, color = circleColor) {
                Text(
                    text = index.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = textColor,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (!isLast) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = lineAlpha)
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = if (focusState.isActive) 1.5.dp else 1.dp,
                    color = if (focusState.isActive)
                        MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha)
                    else
                        MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ) {
            when (step) {

                is SolutionStep.Theory -> {
                    StepTitle(step.title, Icons.Default.Info, focusState)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        step.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is SolutionStep.Formula -> {
                    StepTitle(step.description, ImageVector.vectorResource(R.drawable.science), focusState)
                    Spacer(Modifier.height(12.dp))
                    FormulaBox(step.expression)
                }

                is SolutionStep.Substitution -> {
                    StepTitle(step.description, ImageVector.vectorResource(R.drawable.bolt), focusState)
                    Spacer(Modifier.height(8.dp))
                    FormulaBox(step.expression)
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        LatexAutoWrap(
                            latex = step.result,
                            modifier = Modifier.padding(12.dp),
                            config = LatexConfig(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                darkColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                is SolutionStep.Result -> {
                    StepTitle("Ответ", Icons.Default.CheckCircle, focusState)
                    Spacer(Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(16.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            step.quantities.forEach { q ->
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        q.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Latex(
                                        latex = "${q.symbol} = ${"%.3g".format(q.value)} \\text{${q.unit}}",
                                        config = LatexConfig(
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            darkColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun StepTitle(
    title: String,
    icon: ImageVector,
    focusState: StepFocusState
) {
    val iconColor = when {
        focusState.isActive -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun FormulaBox(expression: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        LatexAutoWrap(
            latex = expression,
            modifier = Modifier.padding(12.dp),
            config = LatexConfig(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                darkColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}