package com.imglmd.physicsexps.presentation.screens.compare

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CompareScreen(
    runIds: List<Int>,
    viewModel: CompareViewModel = koinViewModel { parametersOf(runIds) }
) {

}