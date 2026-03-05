package com.imglmd.physicsexps.presentation.screens.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.core.theme.Parchment
import com.imglmd.physicsexps.presentation.core.theme.White
import com.imglmd.physicsexps.presentation.screens.experiment.components.ExperimentTextField

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),

) {
    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = "Название",
                subtitle = "Категория",
                navigateBack = { }
            )
        }
    ) { innerPadding ->

        Column(Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

            ExperimentTextField(
                state = rememberTextFieldState(),
                label = "Амплитуда",
                symbol = "l",
                unit = "метр"
            )
            ExperimentTextField(
                state = rememberTextFieldState(),
                label = "Амплитуда",
                symbol = "A",
                unit = "метр"
            )
            ExperimentTextField(
                state = rememberTextFieldState(),
                label = "Амплитуда",
                symbol = "T",
                unit = "метр"
            )
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(1f)){
                PrimaryButton(
                    text = "Начать",
                    onClick = {  }
                )
            }
        }


    }
}