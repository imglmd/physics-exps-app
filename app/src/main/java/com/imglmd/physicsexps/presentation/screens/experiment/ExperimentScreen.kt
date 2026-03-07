package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.screens.experiment.components.ExperimentTextField
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExperimentScreen(
    id: String,
    navigateBack: () -> Unit,
    navigateToResult: () -> Unit,
    viewModel: ExperimentViewModel = koinViewModel{ parametersOf(id) }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is ExperimentContract.Action.NavigateToResult -> navigateToResult()
                ExperimentContract.Action.NavigateBack -> navigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = state.experiment.name,
                subtitle = state.experiment.category,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        Column(Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(innerPadding)) {

            state.experiment.inputFields.forEach { field ->
                val textState = rememberTextFieldState(
                    initialText = (state.inputs[field.key] ?: "")
                )

                ExperimentTextField(
                    state = textState,
                    label = field.label,
                    symbol = field.symbol,
                    unit = field.unit,
                    modifier = Modifier.fillMaxWidth()
                )

                LaunchedEffect(textState.text) {
                    viewModel.onIntent(
                        ExperimentContract.Intent.ChangeValue(
                            key = field.key,
                            newValue = textState.text.toString()
                        )
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = "Начать",
                onClick = { viewModel.onIntent(ExperimentContract.Intent.Start) }
            )
        }
    }
}