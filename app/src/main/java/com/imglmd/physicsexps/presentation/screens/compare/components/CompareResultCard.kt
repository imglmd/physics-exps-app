package com.imglmd.physicsexps.presentation.screens.compare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
import com.imglmd.physicsexps.presentation.core.getStringByKey
import kotlin.math.abs

@Composable
fun CompareResultsCard(
    quantities1: List<PhysicalQuantity>,
    quantities2: List<PhysicalQuantity>,
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val allSymbols = (quantities1.map { it.symbol } + quantities2.map { it.symbol }).distinct()
    val map1 = quantities1.associateBy { it.symbol }
    val map2 = quantities2.associateBy { it.symbol }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, colors.outlineVariant, RoundedCornerShape(24.dp))
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.results),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSurface,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        allSymbols.forEachIndexed { index, symbol ->
            QuantityCompareRow(
                q1 = map1[symbol],
                q2 = map2[symbol],
                symbol = symbol,
                color1 = color1,
                color2 = color2
            )
            if (index != allSymbols.lastIndex) {
                HorizontalDivider(color = colors.outline.copy(alpha = 0.42f))
            }
        }
    }
}

@Composable
private fun QuantityCompareRow(
    q1: PhysicalQuantity?,
    q2: PhysicalQuantity?,
    symbol: String,
    color1: Color,
    color2: Color
) {
    val colors = MaterialTheme.colorScheme
    val label = q1?.label ?: q2?.label ?: symbol
    val unit = getStringByKey(q1?.unit ?: q2?.unit ?: "")
    val v1 = q1?.value
    val v2 = q2?.value

    val diffPercent = if (v1 != null && v2 != null && v1 != 0.0) {
        ((v2 - v1) / abs(v1)) * 100
    } else null

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                color = colors.primary,
                modifier = Modifier.padding(end = 6.dp)
            )
            Text(
                text = getStringByKey(label),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (diffPercent != null) {
                DiffBadge(diffPercent = diffPercent, favorsRun2 = v2!! > v1!!, color1 = color1, color2 = color2)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ValueColumn(value = v1, unit = unit, accentColor = color1, modifier = Modifier.weight(1f))
            ValueColumn(value = v2, unit = unit, accentColor = color2, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ValueColumn(
    value: Double?,
    unit: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(accentColor)
        )
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = if (value != null) formatDouble(value) else "—",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (value != null) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            if (unit.isNotBlank() && value != null) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun DiffBadge(
    diffPercent: Double,
    favorsRun2: Boolean,
    color1: Color,
    color2: Color
) {
    val absDiff = abs(diffPercent)
    if (absDiff < 0.05) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "=",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
        return
    }

    val badgeColor = if (favorsRun2) color2 else color1
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(badgeColor.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "${if (diffPercent > 0) "+" else ""}${"%.1f".format(diffPercent)}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = badgeColor,
            fontSize = 11.sp
        )
    }
}

private fun formatDouble(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString()
    else "%.4g".format(value)