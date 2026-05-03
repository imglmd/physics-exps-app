package com.imglmd.physicsexps.presentation.screens.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.model.HistoryFilter
import com.imglmd.physicsexps.presentation.model.SortOrder
import com.imglmd.physicsexps.presentation.screens.history.HistoryContract
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun formatDateRange(from: Long?, to: Long?): String {
    val fmt = SimpleDateFormat("d MMM", Locale.getDefault())
    return when {
        from != null && to != null -> "${fmt.format(Date(from))} — ${fmt.format(Date(to))}"
        from != null -> "c ${fmt.format(Date(from))}"
        to != null -> "по ${fmt.format(Date(to))}"
        else -> "Период"
    }
}

@Composable
fun FilterChipsRow(
    state: HistoryContract.State,
    onIntent: (HistoryContract.Intent) -> Unit,
    onDateChipClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val hasDateFilter = state.filter.dateFrom != null || state.filter.dateTo != null

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            FilterChip(
                selected = state.filter.sortOrder != SortOrder.DATE_DESC,
                onClick = {
                    val next = when (state.filter.sortOrder) {
                        SortOrder.DATE_DESC  -> SortOrder.DATE_ASC
                        SortOrder.DATE_ASC   -> SortOrder.EXPERIMENT
                        SortOrder.EXPERIMENT -> SortOrder.DATE_DESC
                    }
                    onIntent(HistoryContract.Intent.SetSortOrder(next))
                },
                label = {
                    Text(when (state.filter.sortOrder) {
                        SortOrder.DATE_DESC  -> "Сначала новые"
                        SortOrder.DATE_ASC   -> "Сначала старые"
                        SortOrder.EXPERIMENT -> "По эксперименту"
                    })
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (state.filter.sortOrder != SortOrder.DATE_DESC) colors.primary else MaterialTheme.colorScheme.outlineVariant
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface,
                    labelColor = colors.onSurface,
                    selectedContainerColor = colors.primaryContainer,
                    selectedLabelColor = colors.onPrimaryContainer,
                    selectedLeadingIconColor = colors.onPrimaryContainer
                ),
                leadingIcon = {
                    Icon(ImageVector.vectorResource(R.drawable.sort), null, Modifier.size(16.dp))
                }
            )
        }

        item {
            FilterChip(
                selected = hasDateFilter,
                onClick = onDateChipClick,
                label = {
                    Text(
                        if (hasDateFilter) formatDateRange(state.filter.dateFrom, state.filter.dateTo)
                        else "Период"
                    )
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (hasDateFilter) colors.primary else MaterialTheme.colorScheme.outlineVariant
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface,
                    labelColor = colors.onSurface,
                    selectedContainerColor = colors.primaryContainer,
                    selectedLabelColor = colors.onPrimaryContainer,
                    selectedLeadingIconColor = colors.onPrimaryContainer,
                    selectedTrailingIconColor = colors.onPrimaryContainer
                ),
                leadingIcon = {
                    Icon(Icons.Outlined.DateRange, null, Modifier.size(16.dp))
                },
                trailingIcon = if (hasDateFilter) {
                    {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else null
            )
        }

        items(state.availableExperiments) { experiment ->
            FilterChip(
                selected = state.filter.experimentId == experiment.id,
                onClick = {
                    val newId = if (state.filter.experimentId == experiment.id) null
                    else experiment.id
                    onIntent(HistoryContract.Intent.SetExperimentFilter(newId))
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface,
                    labelColor = colors.onSurface,
                    selectedContainerColor = colors.primaryContainer,
                    selectedLabelColor = colors.onPrimaryContainer
                ),
                label = { Text(experiment.name) },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (state.filter.experimentId == experiment.id) colors.primary else MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        if (state.filter != HistoryFilter()) {
            item {
                AssistChip(
                    onClick = { onIntent(HistoryContract.Intent.ClearFilters) },
                    label = { Text("Сбросить") },
                    leadingIcon = {
                        Icon(Icons.Default.Close, null, Modifier.size(16.dp))
                    }
                )
            }
        }
    }
}