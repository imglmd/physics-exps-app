package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.screens.result.components.ChartCard
import com.imglmd.physicsexps.presentation.screens.result.components.ResultCard
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResultScreen(
    navigateBack: () -> Unit,
    viewModel: ResultViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val modelProducer = remember { CartesianChartModelProducer() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is ResultContract.State.Loading -> CircularProgressIndicator()

            is ResultContract.State.Error -> Text(
                (state as ResultContract.State.Error).message
            )

            is ResultContract.State.Success -> Content(
                state = state as ResultContract.State.Success,
                navigateBack = navigateBack,
                modelProducer = modelProducer
            )
        }
    }
}

@Composable
private fun Content(
    state: ResultContract.State.Success,
    navigateBack: () -> Unit,
    modelProducer: CartesianChartModelProducer
) {
    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = state.result.experiment.name,
                subtitle = state.result.experiment.category,
                navigateBack = navigateBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {

            ResultCard(state)

            Spacer(Modifier.height(8.dp))

            ChartCard(state, modelProducer)
        }
    }
}