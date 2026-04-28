@file:OptIn(ExperimentalLayoutApi::class)

package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import com.imglmd.physicsexps.presentation.screens.result.components.CommentSection
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
    navigateSolution: () -> Unit,
    viewModel: ResultViewModel = koinViewModel { parametersOf(runId) }
) {
    val state by viewModel.state.collectAsState()
    val registry = koinInject<ExperimentRegistry>()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ResultContract.Effect.NavigateBack -> navigateBack()
                ResultContract.Effect.NavigateHome -> navigateHome()
                is ResultContract.Effect.NavigateExperiment ->
                    navigateExperiment(effect.id, effect.inputs)
                is ResultContract.Effect.NavigateChart ->
                    navigateChart(effect.runId)
                ResultContract.Effect.NavigateSolution -> navigateSolution()
            }
        }
    }

    when (val s = state) {
        ResultContract.State.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ResultContract.State.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = s.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        is ResultContract.State.Success -> {
            Content(
                state = s,
                registry = registry,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
private fun Content(
    state: ResultContract.State.Success,
    registry: ExperimentRegistry,
    onIntent: (ResultContract.Intent) -> Unit
) {
    val experiment = remember(state.result.experimentId) {
        registry.getById(state.result.experimentId)
    }

    val hasSolution = remember(state.result.experimentId) {
        registry.getById(state.result.experimentId)
            .getSolutionSteps(null)
            .isNotEmpty()
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = experiment.name,
                subtitle = experiment.category,
                navigateBack = { onIntent(ResultContract.Intent.Back) }
            )
        },
        bottomBar = {
            BottomActions(
                onDelete = { onIntent(ResultContract.Intent.Delete) },
                onSave = { onIntent(ResultContract.Intent.Save) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 24.dp)
                .imePadding()
        ) {
            Spacer(Modifier.height(16.dp))

            ResultCard(
                state = state,
                hasSolution = hasSolution,
                navigateSolution = { onIntent(ResultContract.Intent.OpenSolution) },
                onChangeClick = { onIntent(ResultContract.Intent.Change) }
            )

            if (state.result.points.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))

                ChartCard(
                    points = state.result.points,
                    xLabel = state.result.xLabel,
                    yLabel = state.result.yLabel,
                    modelProducer = modelProducer,
                    onChartClick = { onIntent(ResultContract.Intent.OpenChart) }
                )
            }

            Spacer(Modifier.height(20.dp))

            CommentSection(
                comments = state.comments,
                onAddComment = { onIntent(ResultContract.Intent.AddComment(it)) },
                onClickDelete = { onIntent(ResultContract.Intent.DeleteComment(it)) }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BottomActions(
    onDelete: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .navigationBarsPadding()
    ) {

        PrimaryButton(
            text = "Удалить",
            icon = Icons.Outlined.Delete,
            onClick = onDelete,
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
            onClick = onSave,
            iconPosition = IconPosition.End,
            modifier = Modifier.weight(2f)
        )
    }
}