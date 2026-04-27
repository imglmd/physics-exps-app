package com.imglmd.physicsexps.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.model.HistoryItemUi
import com.imglmd.physicsexps.presentation.screens.history.components.HistoryCard
import com.imglmd.physicsexps.presentation.screens.home.components.HomeHistoryCard
import com.imglmd.physicsexps.presentation.screens.home.components.SeeAllCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HistorySection(
    history: List<HistoryItemUi>,
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
            TextButton(onClick = onSeeAllClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = "Все",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(history) { item ->
                HomeHistoryCard(
                    item = item,
                    onClick = { onItemClick(item.resultId) },
                    modifier = Modifier.fillParentMaxHeight()
                )
            }
            item {
                SeeAllCard(onClick = onSeeAllClick, modifier = Modifier.fillParentMaxHeight())
            }
        }
    }
}
