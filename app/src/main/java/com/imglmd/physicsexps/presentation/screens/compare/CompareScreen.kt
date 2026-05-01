package com.imglmd.physicsexps.presentation.screens.compare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.presentation.normalizePoints
import com.imglmd.physicsexps.presentation.screens.compare.components.CompareChartCard
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Сравнение",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(CompareContract.Intent.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                    CompareContent(items = s.items)
                }
            }
        }
    }
}

@Composable
private fun CompareContent(items: List<CompareItem>) {
    if (items.size < 2) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Недостаточно данных для сравнения")
        }
        return
    }

    val run1 = items[0]
    val run2 = items[1]

    val lineColor1 = MaterialTheme.colorScheme.primary
    val lineColor2 = MaterialTheme.colorScheme.secondary

    val modelProducer = remember { CartesianChartModelProducer() }

    val normalized1 = remember(run1.result.points) { normalizePoints(run1.result.points) }
    val normalized2 = remember(run2.result.points) { normalizePoints(run2.result.points) }

    LaunchedEffect(normalized1, normalized2) {
        if (normalized1.isEmpty() && normalized2.isEmpty()) return@LaunchedEffect
        modelProducer.runTransaction {
            lineSeries {
                if (normalized1.isNotEmpty())
                    series(normalized1.map { it.first }, normalized1.map { it.second })
                if (normalized2.isNotEmpty())
                    series(normalized2.map { it.first }, normalized2.map { it.second })
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        CompareChartCard(
            xLabel = run1.result.xLabel,
            yLabel = run1.result.yLabel,
            modelProducer = modelProducer,
            color1 = lineColor1,
            color2 = lineColor2
        )

        Spacer(Modifier.height(8.dp))
    }
}
