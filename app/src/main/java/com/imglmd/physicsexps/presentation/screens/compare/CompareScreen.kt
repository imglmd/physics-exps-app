package com.imglmd.physicsexps.presentation.screens.compare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CompareScreen(
    runIds: List<Int>,
    navigateBack: () -> Unit,
    viewModel: CompareViewModel = koinViewModel { parametersOf(runIds) }
) {
    LaunchedEffect(Unit) {
        viewModel.action.collect { effect ->
            when(effect){
                CompareContract.Action.NavigateBack -> navigateBack()
            }
        }
    }

}