@file:OptIn(ExperimentalMaterial3Api::class)

package com.imglmd.physicsexps.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.screens.history.components.HistoryCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryScreen(
    navigateBack: () -> Unit,
    navigateToResult: (runId: Int) -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { effect ->
            when (effect) {
                HistoryContract.Action.NavigateBack -> navigateBack()
                is HistoryContract.Action.NavigateToResult -> navigateToResult(effect.resultId)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "История",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is HistoryContract.State.Error -> Text(s.message)

            HistoryContract.State.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is HistoryContract.State.Success -> Content(
                state = s,
                onItemClick = {
                    viewModel.onIntent(HistoryContract.Intent.NavigateToResult(it))
                },
                padding = innerPadding,
                isLoading = s.isLoading
            )
        }
    }
}

@Composable
private fun Content(
    state: HistoryContract.State.Success,
    isLoading: Boolean,
    onItemClick: (id: Int) -> Unit,
    padding: PaddingValues = PaddingValues()
) {
    Box(Modifier.fillMaxSize()) {

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 20.dp,
                bottom = padding.calculateBottomPadding(),
                start = 24.dp,
                end = 24.dp
            )
        ) {
            items(
                items = state.history,
                key = { it.id }
            ) { item ->
                HistoryCard(item, onClick = { onItemClick(item.id) })
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = padding.calculateTopPadding()),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}