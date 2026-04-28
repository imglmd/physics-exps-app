package com.imglmd.physicsexps.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.presentation.rememberShimmerBrush

@Composable
fun HomeHistoryPlaceholder(
    modifier: Modifier = Modifier
) {
    val shimmerBrush = rememberShimmerBrush()

    Column(modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(shimmerBrush)
            )

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerBrush)
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(4) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .fillParentMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(shimmerBrush)
                )
            }
        }
    }
}