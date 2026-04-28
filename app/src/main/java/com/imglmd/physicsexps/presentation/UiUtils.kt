package com.imglmd.physicsexps.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

fun Double.round4(): Double {
    return kotlin.math.round(this * 10_000) / 10_000
}

fun normalizePoints(
    points: List<Pair<Double, Double>>
): List<Pair<Double, Double>> {
    return points.map { (x, y) ->
        x.round4() to y.round4()
    }
}


fun downsamplePoints(
    points: List<Pair<Double, Double>>,
    maxPoints: Int = 100
): List<Pair<Double, Double>> {
    if (points.size <= maxPoints) return points

    val step = points.size.toFloat() / maxPoints

    return List(maxPoints) { i ->
        points[(i * step).toInt()]
    }
}


@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    val colors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )

    return Brush.linearGradient(
        colors = colors,
        start = Offset(translateAnim.value - 300f, 0f),
        end = Offset(translateAnim.value, 0f)
    )
}