package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun ResultScreen(
    navigateBack: () -> Unit,
    viewModel: ResultViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(val currentState = state){
            is ResultContract.State.Error -> Text(currentState.message)
            ResultContract.State.Loading -> CircularProgressIndicator()
            is ResultContract.State.Success -> Content(currentState, navigateBack)
        }
    }


}

@Composable
private fun Content(
    state: ResultContract.State.Success,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = state.result.experiment.name,
                subtitle = state.result.experiment.category,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Результаты вычислений", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary)
                state.result.quantities.forEach { quantity ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${quantity.label} (${quantity.symbol})",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${round(quantity.value * 1000) / 1000} ${quantity.unit}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ResultItem(
    title: String,
){

}