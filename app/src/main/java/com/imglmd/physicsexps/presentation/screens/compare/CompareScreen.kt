package com.imglmd.physicsexps.presentation.screens.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.presentation.alignSeries
import com.imglmd.physicsexps.presentation.normalizePoints
import com.imglmd.physicsexps.presentation.screens.compare.components.CompareChartCard
import com.imglmd.physicsexps.presentation.screens.compare.components.CompareResultsCard
import com.imglmd.physicsexps.presentation.screens.compare.components.CompareRunCard
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.components.IconPosition
import com.imglmd.physicsexps.presentation.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    runIds: List<Int>,
    navigateBack: () -> Unit,
    viewModel: CompareViewModel = koinViewModel { parametersOf(runIds) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                CompareContract.Action.NavigateBack -> navigateBack()
            }
        }
    }

    Scaffold(
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            when (val s = state) {
                is CompareContract.State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CompareContract.State.Error -> {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }

                is CompareContract.State.Success -> {
                    CompareContent(items = s.items, navigateBack, padding)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.67f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                                Color.Transparent,
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun CompareContent(items: List<CompareItem>, navigateBack: () -> Unit, padding: PaddingValues) {
    if (items.size < 2) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.insufficient_data))
        }
        return
    }

    val run1 = items[0]
    val run2 = items[1]

    val color1 = MaterialTheme.colorScheme.primary
    val color2 = MaterialTheme.colorScheme.secondary

    val modelProducer = remember { CartesianChartModelProducer() }

    val normalized1 = remember(run1.result.points) { normalizePoints(run1.result.points) }
    val normalized2 = remember(run2.result.points) { normalizePoints(run2.result.points) }

    val (aligned1, aligned2) = remember(normalized1, normalized2) {
        alignSeries(normalized1, normalized2)
    }

    LaunchedEffect(aligned1, aligned2) {
        if (aligned1.isEmpty() && aligned2.isEmpty()) return@LaunchedEffect

        modelProducer.runTransaction {
            lineModel {
                if (aligned1.isNotEmpty()) series(
                    aligned1.map { it.first },
                    aligned1.map { it.second })
                if (aligned2.isNotEmpty()) series(
                    aligned2.map { it.first },
                    aligned2.map { it.second })
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CompareRunCard(
                item = run1,
                label = stringResource(R.string.run_1),
                accentColor = color1,
                modifier = Modifier.weight(1f)
            )
            CompareRunCard(
                item = run2,
                label = stringResource(R.string.run_2),
                accentColor = color2,
                modifier = Modifier.weight(1f)
            )
        }
        CompareResultsCard(
            quantities1 = run1.result.quantities,
            quantities2 = run2.result.quantities,
            color1 = color1,
            color2 = color2
        )

        CompareChartCard(
            xLabel = run1.result.xLabel,
            yLabel = run1.result.yLabel,
            modelProducer = modelProducer,
            color1 = color1,
            color2 = color2
        )

        PrimaryButton(
            text = stringResource(R.string.go_back),
            onClick = navigateBack,
            icon = Icons.AutoMirrored.Default.KeyboardArrowLeft,
            iconPosition = IconPosition.EdgeStart
        )
    }
}
