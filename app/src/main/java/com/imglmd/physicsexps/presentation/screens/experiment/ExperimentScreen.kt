package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.screens.experiment.components.ExperimentTextField
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentScreen(
    id: String,
    navigateBack: () -> Unit,
    navigateToResult: () -> Unit,
    viewModel: ExperimentViewModel = koinViewModel { parametersOf(id) }
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

    val textStates = remember(state.experiment.inputFields) {
        state.experiment.inputFields.associate { field ->
            field.key to TextFieldState(initialText = "")
        }
    }
    state.experiment.inputFields.forEach { field ->
        val textState = textStates[field.key]!!
        LaunchedEffect(textState) {
            snapshotFlow { textState.text.toString() }
                .collect { value ->
                    viewModel.onIntent(
                        ExperimentContract.Intent.ChangeValue(
                            key = field.key,
                            newValue = value
                        )
                    )
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize().imePadding(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item {
                    CarouselSection(state)
                    Text(
                        text = state.experiment.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Введите известные величины:",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(
                    state.experiment.inputFields,
                    key = { it. key }
                ) { field ->
                    val textState = textStates[field.key]!!

                    ExperimentTextField(
                        state = textState,
                        label = field.label,
                        symbol = field.symbol,
                        unit = field.unit,
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = if (state.error != null) MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                }
                state.error?.let { error ->
                    item {
                        Text(
                            error,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }

            PrimaryButton(
                text = "Начать",
                isLoading = state.isLoading,
                enabled = state.isButtonActive,
                onClick = { viewModel.onIntent(ExperimentContract.Intent.Start) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselSection(
    state: ExperimentContract.State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val carouselState = rememberCarouselState() { 4 }

        HorizontalMultiBrowseCarousel(
            state = carouselState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp)),
            preferredItemWidth = 200.dp,
            itemSpacing = 8.dp
        ) {

            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .height(140.dp)
                    .maskClip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { index ->

                val selected = carouselState.currentItem == index

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (selected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
    }
}