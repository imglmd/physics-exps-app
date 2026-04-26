package com.imglmd.physicsexps.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navigateToExperiment: (String) -> Unit,
    navigateToResult: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { effect ->
            when (effect) {
                is HomeAction.NavigateToResult -> navigateToResult(effect.runId)
                is HomeAction.NavigateToExperiment -> navigateToExperiment(effect.id)
            }
        }
    }

    Scaffold() { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            //verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 20.dp,
                bottom = innerPadding.calculateBottomPadding(),
                start = 24.dp,
                end = 24.dp
            )
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                val searchState = rememberTextFieldState()

                LaunchedEffect(searchState.text) {
                    viewModel.onIntent(HomeIntent.ChangeSearchText(searchState.text.toString()))
                }

                Column {
                    if (state.history.isNotEmpty()){
                        HistorySection(
                            history = state.history,
                            onSeeAllClick = {  },
                            onItemClick = { viewModel.onIntent(HomeIntent.NavigateToRunResult(it)) }
                        )

                        Spacer(Modifier.height(30.dp))

                    }
                    SearchTextField(
                        state = searchState
                    )

                }

            }
            state.experimentsByCategory.forEach { (category, experiments) ->

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 22.dp, bottom = 6.dp)
                    )
                }

                items(experiments) { experiment ->
                    Column {
                        ExperimentItem(
                            name = experiment.name,
                            imageRes = experiment.imageRes,
                            onClick = { viewModel.onIntent(HomeIntent.NavigateToExperiment(experiment.id)) }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ExperimentItem(
    name: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // затемнение картинки
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Color.Black.copy(
                        alpha = if (isSystemInDarkTheme()) 0.15f else 0f
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = if (isSystemInDarkTheme()) 0.6f else 0.3f)
                        )
                    )
                )
        ) {
            Text(
                text = name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
private fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.bodyLarge

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(100)
            )
            .clip(RoundedCornerShape(100))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = 16.dp, end = 4.dp)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable .search),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            state = state,
            textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.weight(1f),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = { innerTextField ->

                Box(
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (state.text.isEmpty()) {
                        Text(
                            text = "Поиск",
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    innerTextField()
                }
            }
        )

        if (state.text.isNotEmpty()) {
            IconButton(
                onClick = { state.clearText() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cross),
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}