package com.imglmd.physicsexps.presentation.screens.history.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill


@Composable
fun HistoryChartCard(
    points: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
    val fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(70.dp)
    ) {
        // сетка
        val stepX = size.width / 4
        val stepY = size.height / 3
        for (i in 1..3) drawLine(gridColor, Offset(0f, stepY * i), Offset(size.width, stepY * i), 1.dp.toPx())
        for (i in 1..4) drawLine(gridColor, Offset(stepX * i, 0f), Offset(stepX * i, size.height), 1.dp.toPx())

        if (points.size < 2) return@Canvas

        val minX = points.minOf { it.first }
        val maxX = points.maxOf { it.first }
        val minY = points.minOf { it.second }
        val maxY = points.maxOf { it.second }
        val rangeX = (maxX - minX).takeIf { it != 0.0 } ?: 1.0
        val rangeY = (maxY - minY).takeIf { it != 0.0 } ?: 1.0

        val topPadding = 2.dp.toPx()
        val bottomPadding = 2.dp.toPx()
        val drawableHeight = size.height - topPadding - bottomPadding

        fun toOffset(p: Pair<Double, Double>) = Offset(
            x = ((p.first - minX) / rangeX * size.width).toFloat(),
            y = (size.height - bottomPadding - ((p.second - minY) / rangeY * drawableHeight)).toFloat()
        )

        val offsets = points.map { toOffset(it) }

        // заливка под линией
        /*val fillPath = Path().apply {
            moveTo(offsets.first().x, size.height)
            offsets.forEach { lineTo(it.x, it.y) }
            lineTo(offsets.last().x, size.height)
            close()
        }
        drawPath(fillPath, color = fillColor)*/

        // линия
        val linePath = Path().apply {
            moveTo(offsets.first().x, offsets.first().y)
            offsets.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(
            path = linePath,
            color = primaryColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}