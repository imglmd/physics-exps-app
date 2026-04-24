package com.imglmd.physicsexps.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.screens.result.ResultContract
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.common.Fill


@Composable
fun ChartCard(
    state: ResultContract.State.Success,
    modelProducer: CartesianChartModelProducer
) {
    val points = state.result.points ?: return

    val colors = MaterialTheme.colorScheme

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(color = colors.onSurface)
    )

    val chart = rememberChart(state, marker)

    LaunchedEffect(points) {
        modelProducer.runTransaction {
            val (x, y) = points.unzip()
            lineSeries { series(x, y) }
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.08f),
                spotColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.42f)
            )
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.primaryContainer
        )
    ) {
        CartesianChartHost(
            chart = chart,
            modelProducer = modelProducer,
            scrollState = rememberVicoScrollState(),
            zoomState = rememberVicoZoomState(),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.primaryContainer)
                .padding(8.dp)
        )
    }
}
@Composable
private fun rememberChart(
    state: ResultContract.State.Success,
    marker: CartesianMarker
) = rememberCartesianChart(

    rememberLineCartesianLayer(
        lineProvider = LineCartesianLayer.LineProvider.series(
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(
                    Fill(MaterialTheme.colorScheme.primary.toArgb())
                ),
                stroke = LineCartesianLayer.LineStroke.continuous(3.dp),
                pointConnector = LineCartesianLayer.PointConnector.cubic(0.001f)
            )
        )
    ),

    startAxis = VerticalAxis.rememberStart(
        title = state.result.yLabel,

        label = rememberTextComponent(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        titleComponent = rememberTextComponent(
            color = MaterialTheme.colorScheme.primary
        ),

        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f).toArgb()),
            thickness = 1.dp
        )
    ),

    bottomAxis = HorizontalAxis.rememberBottom(
        title = state.result.xLabel,

        label = rememberTextComponent(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        titleComponent = rememberTextComponent(
            color = MaterialTheme.colorScheme.primary
        ),

        guideline = rememberLineComponent(
            fill = Fill(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f).toArgb()),
            thickness = 1.dp
        )
    ),

    marker = marker
)