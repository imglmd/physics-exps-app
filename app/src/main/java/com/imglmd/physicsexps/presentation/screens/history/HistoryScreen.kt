@file:OptIn(ExperimentalMaterial3Api::class)

package com.imglmd.physicsexps.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.model.HistoryFilter
import com.imglmd.physicsexps.presentation.navigation.HistoryMode
import com.imglmd.physicsexps.presentation.screens.history.components.FilterChipsRow
import com.imglmd.physicsexps.presentation.screens.history.components.HistoryCard
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HistoryScreen(
    mode: HistoryMode = HistoryMode.NORMAL,
    preselectedIds: List<Int>,
    navigateBack: () -> Unit,
    navigateToResult: (runId: Int) -> Unit,
    onSelectRuns: (ids: List<Int>) -> Unit,
    viewModel: HistoryViewModel = koinViewModel{ parametersOf(preselectedIds) }
) {
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { effect ->
            when (effect) {
                HistoryContract.Action.NavigateBack -> navigateBack()
                is HistoryContract.Action.NavigateToResult -> navigateToResult(effect.resultId)
                is HistoryContract.Action.ReturnSelection -> onSelectRuns(effect.ids)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (mode == HistoryMode.SELECTION) "Выберите эксперименты" else "История",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onIntent(HistoryContract.Intent.ShowDeleteDialog) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Удалить всё"
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
        Content(
            mode = mode,
            state = state,
            onItemClick = {
                if (mode == HistoryMode.SELECTION) viewModel.onIntent(HistoryContract.Intent.ToggleSelection(it))
                else viewModel.onIntent(HistoryContract.Intent.NavigateToResult(it)) },
            padding = innerPadding,
            isLoading = state.isLoading,
            onIntent = viewModel::onIntent,
            onDateChipClick = {
                if (state.filter.dateFrom != null || state.filter.dateTo != null) {
                    viewModel.onIntent(
                        HistoryContract.Intent.SetDateRange(null, null)
                    )
                } else showDatePicker = true
            }
        )
    }

    if (showDatePicker) {
        val pickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = state.filter.dateFrom,
            initialSelectedEndDateMillis = state.filter.dateTo
        )

        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onIntent(
                            HistoryContract.Intent.SetDateRange(
                                from = pickerState.selectedStartDateMillis,
                                to = pickerState.selectedEndDateMillis
                            )
                        )
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    ),
                    enabled = pickerState.selectedStartDateMillis != null
                ) {
                    Text("Применить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DateRangePicker(
                state = pickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    dividerColor = MaterialTheme.colorScheme.outline,
                    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                ),
                title = {

                },
                headline = {
                    Text(
                        text = "Выберите период",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                }
            )
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(HistoryContract.Intent.HideDeleteDialog) },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            title = {
                Text("Удалить историю?", style = MaterialTheme.typography.titleMedium)
            },
            text = {
                Text(
                    text = "Все эксперименты будут удалены без возможности восстановления.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onIntent(HistoryContract.Intent.DeleteAll) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    )
                ) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(HistoryContract.Intent.HideDeleteDialog) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                    )
                ) {
                    Text("Отмена", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

@Composable
private fun Content(
    mode: HistoryMode,
    state: HistoryContract.State,
    isLoading: Boolean,
    onItemClick: (id: Int) -> Unit,
    onIntent: (HistoryContract.Intent) -> Unit,
    onDateChipClick: () -> Unit,
    padding: PaddingValues = PaddingValues()
) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(top = padding.calculateTopPadding() + 4.dp)) {
            FilterChipsRow(
                state = state,
                onIntent = onIntent,
                onDateChipClick = onDateChipClick
            )

            if (state.history.isEmpty() && !isLoading) {
                EmptyHistory(
                    hasFilters = state.filter != HistoryFilter(),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    modifier = Modifier.fillMaxSize(),
                    verticalItemSpacing = 12.dp,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        bottom = padding.calculateBottomPadding(),
                        start = 24.dp,
                        end = 24.dp
                    )
                ) {
                    items(items = state.history, key = { it.id }) { item ->
                        val index = state.selectedIds.indexOf(item.id)
                            .takeIf { it != -1 }
                        HistoryCard(item, onClick = { onItemClick(item.id) }, selectionIndex = index)
                    }
                }
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
        if (mode == HistoryMode.SELECTION){
            PrimaryButton(
                enabled = state.selectedIds.size >= 2,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = padding.calculateBottomPadding() + 10.dp).padding(horizontal = 16.dp),
                text = "Сравнить (${state.selectedIds.size})",
                onClick = { onIntent(HistoryContract.Intent.ConfirmSelection) }
            )
        }
    }
}

@Composable
private fun EmptyHistory(
    hasFilters: Boolean,
    modifier: Modifier = Modifier
) {
    val title = if (hasFilters) {
        "Ничего не найдено"
    } else {
        "Пока нет экспериментов"
    }

    val subtitle = if (hasFilters) {
        "Попробуйте изменить фильтры\nили сбросить их"
    } else {
        "Проведите первый эксперимент —\nрезультаты появятся здесь"
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}