package com.imglmd.physicsexps.presentation.screens.history.components

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
    modelProducer: CartesianChartModelProducer
) {
    val colors = MaterialTheme.colorScheme

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(
            lineProvider = LineCartesianLayer.LineProvider.series(
                LineCartesianLayer.rememberLine(
                    fill = LineCartesianLayer.LineFill.single(
                        Fill(colors.primary)
                    ),
                    stroke = LineCartesianLayer.LineStroke.Continuous(2.dp),
                    pointConnector = LineCartesianLayer.PointConnector.cubic(0.2f)
                )
            )
        )
    )

    LaunchedEffect(points) {
        modelProducer.runTransaction {
            val (x, y) = points.unzip()
            lineSeries { series(x, y) }
        }
    }

    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(70.dp)
            .drawBehind {
                val stepX = size.width / 4
                val stepY = size.height / 3

                for (i in 1..3) {
                    drawLine(
                        color = colors.onSurface.copy(alpha = 0.05f),
                        start = Offset(0f, stepY * i),
                        end = Offset(size.width, stepY * i),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                for (i in 1..4) {
                    drawLine(
                        color = colors.onSurface.copy(alpha = 0.05f),
                        start = Offset(stepX * i, 0f),
                        end = Offset(stepX * i, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    )
}