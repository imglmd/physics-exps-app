package com.imglmd.physicsexps.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.normalizePoints
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent


@Composable
fun ChartCard(
    points: List<Pair<Double, Double>>,
    xLabel: String,
    yLabel: String,
    modelProducer: CartesianChartModelProducer,
    onChartClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val normalized = remember(points) { normalizePoints(points) }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(TextStyle(color = colors.onSurface))
    )

    val chart = rememberChart(xLabel, yLabel, marker)
    val initialZoom = remember { Zoom.min(Zoom.fixed(), Zoom.Content) }
    var resetKey by remember { mutableIntStateOf(0) }

    val zoomState = key(resetKey) { rememberVicoZoomState(initialZoom = initialZoom) }
    val scrollState = key(resetKey) { rememberVicoScrollState() }

    LaunchedEffect(normalized) {
        if (normalized.isEmpty()) return@LaunchedEffect
        modelProducer.runTransaction {
            lineSeries {
                series(
                    normalized.map { it.first },
                    normalized.map { it.second }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            )
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
            Row {
                IconButton(
                    onClick = { resetKey++ },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colors.primary.copy(alpha = 0.1f)
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
                Spacer(Modifier.width(4.dp))
                IconButton(
                    onClick = onChartClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = colors.primary.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.expand_content),
                        contentDescription = "На весь экран",
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
                .height(260.dp)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 12.dp)
        )
    }
}

@Composable
private fun rememberChart(
    xLabel: String,
    yLabel: String,
    marker: CartesianMarker
) = rememberCartesianChart(
    rememberLineCartesianLayer(
        lineProvider = LineCartesianLayer.LineProvider.series(
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(
                    Fill(MaterialTheme.colorScheme.primary)
                ),
                stroke = LineCartesianLayer.LineStroke.Continuous(3.dp),
                pointConnector = LineCartesianLayer.PointConnector.cubic(0.001f)
            )
        )
    ),

    startAxis = VerticalAxis.rememberStart(
        title = { yLabel },
        label = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        ),
        titleComponent = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.primary)
        ),
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            thickness = 1.dp
        )
    ),

    bottomAxis = HorizontalAxis.rememberBottom(
        title = { xLabel },
        label = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        ),
        titleComponent = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.primary)
        ),
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            thickness = 1.dp
        ),
        itemPlacer = remember { HorizontalAxis.ItemPlacer.aligned(spacing = { 3 }) }
    ),

    marker = marker
)