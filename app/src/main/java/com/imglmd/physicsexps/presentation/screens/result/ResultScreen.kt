package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.IconPosition
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.screens.result.components.ChartCard
import com.imglmd.physicsexps.presentation.screens.result.components.ResultCard
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResultScreen(
    runId: Int?,
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    navigateChart: (Int) -> Unit,
    navigateExperiment: (String, Map<String, String>) -> Unit,
    viewModel: ResultViewModel = koinViewModel { parametersOf(runId) }
) {
    val state by viewModel.state.collectAsState()

    val modelProducer = remember { CartesianChartModelProducer() }

    val registry = koinInject<ExperimentRegistry>()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ResultContract.Effect.NavigateBack -> navigateBack()
                ResultContract.Effect.NavigateHome -> navigateHome()
                is ResultContract.Effect.NavigateExperiment ->
                    navigateExperiment(effect.id, effect.inputs)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val s = state) {
            is ResultContract.State.Loading -> CircularProgressIndicator()

            is ResultContract.State.Error -> Text(s.message)

            is ResultContract.State.Success -> Content(
                state = s,
                isFromHistory = runId != null,
                registry = registry,
                onBackClick = { viewModel.onIntent(ResultContract.Intent.Back) },
                modelProducer = modelProducer,
                onDeleteClick = { viewModel.onIntent(ResultContract.Intent.Delete) },
                onSaveClick = navigateHome,
                onChangeClick = { viewModel.onIntent(ResultContract.Intent.Change) },
                onChartClick = {
                    val id = viewModel.getRunId() ?: return@Content
                    navigateChart(id)
                }
            )
        }
    }
}

@Composable
private fun Content(
    state: ResultContract.State.Success,
    registry: ExperimentRegistry,
    isFromHistory: Boolean,
    modelProducer: CartesianChartModelProducer,

    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onChangeClick: () -> Unit,
    onChartClick: () -> Unit
) {
    val experiment = remember(state.result.experimentId) {
        registry.getById(state.result.experimentId)
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = experiment.name,
                subtitle = experiment.category,
                navigateBack = onBackClick
            )
        }
    ) { padding ->

        Box(Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 100.dp)
            ) {
                ResultCard(state, onChangeClick)

                if (state.result.points.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    ChartCard(
                        state.result.points,
                        state.result.xLabel,
                        state.result.yLabel,
                        modelProducer,
                        onChartClick
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                PrimaryButton(
                    text = "Удалить",
                    icon = Icons.Outlined.Delete,
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1.5f),
                )

                Spacer(Modifier.width(10.dp))

                PrimaryButton(
                    text = "Сохранить",
                    icon = Icons.Outlined.Done,
                    onClick = onSaveClick,
                    iconPosition = IconPosition.End,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}