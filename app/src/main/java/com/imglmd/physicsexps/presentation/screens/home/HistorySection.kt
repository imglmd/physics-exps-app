package com.imglmd.physicsexps.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.domain.model.ExperimentResult


@Composable
fun HistorySection(
    history: List<ExperimentResult>,     //TODO заменить на ExperimentRuns
    onSeeAllClick: () -> Unit,
    onItemClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Последнее",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Все",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onSeeAllClick()
                }
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(history) { item ->
                HistoryCard(
                    result = item,
                    onClick = { onItemClick(0) }
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    result: ExperimentResult,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = result.experiment.name,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            result.quantities.take(2).forEach {

                Text(
                    text = "${it.symbol} = ${it.value} ${it.unit}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}