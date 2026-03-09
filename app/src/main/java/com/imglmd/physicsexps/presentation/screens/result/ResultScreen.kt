package com.imglmd.physicsexps.presentation.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun ResultScreen(
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    viewModel: ResultViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                ResultAction.NavigateBack -> navigateBack()
                ResultAction.NavigateHome -> navigateHome()
            }
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(val currentState = state){
            is ResultState.Error -> Text(currentState.message)
            ResultState.Loading -> CircularProgressIndicator()
            is ResultState.Success -> Content(
                state = currentState,
                navigateBack = navigateBack,
                saveResult = { viewModel.onIntent(ResultIntent.Save) },
                deleteResult = { viewModel.onIntent(ResultIntent.Delete) }
            )
        }
    }


}

@Composable
private fun Content(
    state: ResultState.Success,
    navigateBack: () -> Unit,
    saveResult: () -> Unit,
    deleteResult: () -> Unit,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
            ) {
                item{
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
                        HorizontalDivider(
                            Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.tertiary
                        )
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

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PrimaryButton(
                    text = "Удалить",
                    onClick = deleteResult,
                    modifier = Modifier.weight(1f),
                    borderColor = MaterialTheme.colorScheme.primary,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                )
                PrimaryButton(
                    text = "Сохранить",
                    onClick = saveResult,
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Composable
private fun ResultItem(
    title: String,
){

}