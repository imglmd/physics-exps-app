package com.imglmd.physicsexps.presentation.screens.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.screens.result.ResultContract
import kotlin.math.round

@Composable
fun ResultCard(state: ResultContract.State.Success) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            "Результаты вычислений",
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        state.result.quantities.forEach {
            ResultItem(it.label, it.symbol, it.value, it.unit)
        }
    }
}


@Composable
private fun ResultItem(
    label: String,
    symbol: String,
    value: Double,
    unit: String
) {
    Column {
        Text(
            text = "$label ($symbol)",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${round(value * 1000) / 1000} $unit",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}