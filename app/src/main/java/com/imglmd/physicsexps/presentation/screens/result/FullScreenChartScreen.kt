package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenChartScreen(
    runId: Int,
    navigateBack: () -> Unit,
    viewModel: ResultViewModel = koinViewModel { parametersOf(runId) }
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(state) {
        if (state is ResultContract.State.Error) {
            navigateBack()
        }
    }

    val successState = state as? ResultContract.State.Success ?: return

    val result = successState.result

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.result.experimentId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->

        val points = result.points

        if (points.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет данных для отображения")
            }
            return@Scaffold
        }

        ChartContent(
            points = points,
            xLabel = result.xLabel,
            yLabel = result.yLabel,
            padding = padding
        )
    }
}

@Composable
private fun ChartContent(
    points: List<Pair<Double, Double>>,
    xLabel: String,
    yLabel: String,
    padding: PaddingValues
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val normalized = remember(points) { normalizePoints(points) }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.onSurface)
        )
    )

    LaunchedEffect(normalized) {
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
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding())
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        PointsStatRow(
            points = normalized,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        CartesianChartHost(
            chart = rememberChart(xLabel, yLabel, marker),
            modelProducer = modelProducer,
            scrollState = rememberVicoScrollState(),
            zoomState = rememberVicoZoomState(initialZoom = Zoom.Content),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun PointsStatRow(
    points: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier
) {
    val minY = remember(points) { points.minOf { it.second } }
    val maxY = remember(points) { points.maxOf { it.second } }
    val count = points.size

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatTile(label = "Точек", value = "$count", modifier = Modifier.weight(1f))
        StatTile(label = "Мин", value = "%.3g".format(minY), modifier = Modifier.weight(1f))
        StatTile(label = "Макс", value = "%.3g".format(maxY), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer  )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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