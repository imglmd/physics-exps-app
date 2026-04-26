package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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

@Composable
fun FullScreenChartScreen(
    runId: Int,
    navigateBack: () -> Unit,
    viewModel: ResultViewModel = koinViewModel { parametersOf(runId) }
) {
    val state by viewModel.state.collectAsState()

    val points = (state as? ResultContract.State.Success)?.result?.points ?: emptyList()

    if (points.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Нет данных")
        }
        return
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    val normalized = remember(points) { normalizePoints(points) }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(TextStyle(color = MaterialTheme.colorScheme.onSurface))
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

    Scaffold { padding ->
        CartesianChartHost(
            chart = rememberChart("x", "y", marker),
            modelProducer = modelProducer,
            scrollState = rememberVicoScrollState(),
            zoomState = rememberVicoZoomState(initialZoom = Zoom.Content),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
            TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)
        ),
        titleComponent = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.primary)
        ),
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            thickness = 1.dp
        )
    ),

    bottomAxis = HorizontalAxis.rememberBottom(
        title = { xLabel },
        label = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)
        ),
        titleComponent = rememberTextComponent(
            TextStyle(color = MaterialTheme.colorScheme.primary)
        ),
        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            thickness = 1.dp
        ),
        itemPlacer = remember { HorizontalAxis.ItemPlacer.aligned(spacing = { 3 }) }
    ),

    marker = marker
)