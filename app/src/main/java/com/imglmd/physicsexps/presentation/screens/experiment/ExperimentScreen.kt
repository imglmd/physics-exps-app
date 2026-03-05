package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ExperimentScreen(
    id: Int,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ExperimentViewModel>{ parametersOf(id) }


}