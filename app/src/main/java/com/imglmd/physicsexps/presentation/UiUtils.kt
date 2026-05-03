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
import androidx.compose.ui.graphics.Color

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

// хз как работает, нейронка писала. нужно чтоб на графике сравнения точки по x совпадали
fun alignSeries(
    s1: List<Pair<Double, Double>>,
    s2: List<Pair<Double, Double>>
): Pair<List<Pair<Double, Double>>, List<Pair<Double, Double>>> {

    val xs = (s1.map { it.first } + s2.map { it.first })
        .distinct()
        .sorted()

    fun interpolate(series: List<Pair<Double, Double>>, x: Double): Double? {
        val sorted = series.sortedBy { it.first }

        val exact = sorted.find { it.first == x }
        if (exact != null) return exact.second

        val left = sorted.lastOrNull { it.first < x }
        val right = sorted.firstOrNull { it.first > x }

        if (left != null && right != null) {
            val t = (x - left.first) / (right.first - left.first)
            return left.second + t * (right.second - left.second)
        }

        return null
    }

    val aligned1 = xs.mapNotNull { x ->
        interpolate(s1, x)?.let { x to it }
    }

    val aligned2 = xs.mapNotNull { x ->
        interpolate(s2, x)?.let { x to it }
    }

    return aligned1 to aligned2
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

fun Color.blendWith(background: Color, ratio: Float): Color {
    return Color(
        red = red * ratio + background.red * (1 - ratio),
        green = green * ratio + background.green * (1 - ratio),
        blue = blue * ratio + background.blue * (1 - ratio),
        alpha = 1f
    )
}