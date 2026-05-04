package com.imglmd.physicsexps.presentation.screens.compare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent


@Composable
fun CompareChartCard(
    xLabel: String,
    yLabel: String,
    modelProducer: CartesianChartModelProducer,
    color1: Color,
    color2: Color
) {
    val colors = MaterialTheme.colorScheme
    val initialZoom = remember { Zoom.min(Zoom.fixed(), Zoom.Content) }
    var resetKey by remember { mutableIntStateOf(0) }
    val zoomState = key(resetKey) { rememberVicoZoomState(initialZoom = initialZoom) }
    val scrollState = key(resetKey) { rememberVicoScrollState() }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(TextStyle(color = colors.onSurface))
    )

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(
            lineProvider = LineCartesianLayer.LineProvider.series(
                LineCartesianLayer.rememberLine(
                    fill = LineCartesianLayer.LineFill.single(Fill(color1)),
                    stroke = LineCartesianLayer.LineStroke.Continuous(3.dp),
                    pointConnector = LineCartesianLayer.PointConnector.cubic(0.001f)
                ),
                LineCartesianLayer.rememberLine(
                    fill = LineCartesianLayer.LineFill.single(Fill(color2)),
                    stroke = LineCartesianLayer.LineStroke.Continuous(3.dp),
                    pointConnector = LineCartesianLayer.PointConnector.cubic(0.001f)
                )
            )
        ),
        startAxis = VerticalAxis.rememberStart(
            title = { yLabel },
            label = rememberTextComponent(TextStyle(color = colors.onSurfaceVariant)),
            titleComponent = rememberTextComponent(TextStyle(color = colors.primary)),
            guideline = rememberLineComponent(
                fill = Fill(colors.outline.copy(alpha = 0.4f)),
                thickness = 1.dp
            )
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            title = { xLabel },
            label = rememberTextComponent(TextStyle(color = colors.onSurfaceVariant)),
            titleComponent = rememberTextComponent(TextStyle(color = colors.primary)),
            guideline = rememberLineComponent(
                fill = Fill(colors.outline.copy(alpha = 0.4f)),
                thickness = 1.dp
            ),
            itemPlacer = remember { HorizontalAxis.ItemPlacer.aligned(spacing = { 3 }) }
        ),
        marker = marker
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.outlineVariant, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "График",
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChartLegend(color1 = color1, color2 = color2)

                IconButton(
                    onClick = { resetKey++ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colors.primaryContainer
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Сбросить масштаб",
                        tint = colors.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = colors.outline)

        CartesianChartHost(
            chart = chart,
            modelProducer = modelProducer,
            scrollState = scrollState,
            zoomState = zoomState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 12.dp)
        )
    }
}

@Composable
private fun ChartLegend(color1: Color, color2: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendDot(color = color1, label = "Запуск 1")
        LegendDot(color = color2, label = "Запуск 2")
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}