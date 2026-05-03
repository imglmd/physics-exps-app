package com.imglmd.physicsexps.presentation.screens.compare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imglmd.physicsexps.domain.model.PhysicalQuantity
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
            .border(1.dp, colors.outlineVariant, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Результаты",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )
        }

        HorizontalDivider(color = colors.outline)

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allSymbols.forEach { symbol ->
                val q1 = map1[symbol]
                val q2 = map2[symbol]
                QuantityCompareRow(
                    q1 = q1,
                    q2 = q2,
                    symbol = symbol,
                    color1 = color1,
                    color2 = color2
                )
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
    val unit = q1?.unit ?: q2?.unit ?: ""
    val v1 = q1?.value
    val v2 = q2?.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .border(1.dp, colors.outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                color = colors.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ValueTile(
                value = v1,
                unit = unit,
                accentColor = color1,
                runLabel = "Запуск 1",
                modifier = Modifier.weight(1f)
            )

            DeltaColumn(
                v1 = v1,
                v2 = v2,
                color1 = color1,
                color2 = color2
            )

            ValueTile(
                value = v2,
                unit = unit,
                accentColor = color2,
                runLabel = "Запуск 2",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ValueTile(
    value: Double?,
    unit: String,
    accentColor: Color,
    runLabel: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = runLabel,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontSize = 10.sp
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = if (value != null) formatDouble(value) else "—",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (value != null) colors.onSurface
                else colors.onSurfaceVariant.copy(alpha = 0.4f)
            )
            if (unit.isNotBlank() && value != null) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun DeltaColumn(
    v1: Double?,
    v2: Double?,
    color1: Color,
    color2: Color
) {
    val delta = if (v1 != null && v2 != null) v2 - v1 else null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(52.dp)
    ) {
        Text(
            text = if (delta != null) formatDelta(delta) else "—",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = when {
                v1 == null || v2 == null -> MaterialTheme.colorScheme.onSurfaceVariant
                v2 > v1 -> color2
                v1 > v2 -> color1
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )

        if (v1 != null && v2 != null) {
            DeltaBar(
                v1 = v1,
                v2 = v2,
                color1 = color1,
                color2 = color2
            )
        }
    }
}
@Composable
private fun DeltaBar(
    v1: Double,
    v2: Double,
    color1: Color,
    color2: Color
) {
    val total = (abs(v1) + abs(v2)).coerceAtLeast(1e-6)

    fun ratio(v: Double) = (abs(v) / total).toFloat()

    val r1 = ratio(v1)
    val r2 = ratio(v2)

    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 4.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        fun Modifier.bar(ratio: Float, align: Alignment, color: Color) =
            fillMaxWidth(ratio)
                .fillMaxHeight()
                .align(align)
                .background(color)

        Box(Modifier.bar(r1, Alignment.CenterStart, color1))
        Box(Modifier.bar(r2, Alignment.CenterEnd, color2))
    }
}

private fun formatDouble(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString()
    else "%.4g".format(value)

private fun formatDelta(delta: Double): String {
    val absDelta = abs(delta)

    if (absDelta < 1e-6) return "0"

    return when {
        absDelta >= 1000 -> "%.0f".format(absDelta)
        absDelta >= 1    -> "%.2f".format(absDelta)
        else             -> "%.4f".format(absDelta)
    }.trimEnd('0').trimEnd('.').trimEnd(',')
}