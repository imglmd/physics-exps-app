package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CarouselIndicators(
    count: Int,
    current: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            val selected = current == index

            val dotWidth by animateDpAsState(
                targetValue = if (selected) 20.dp else 6.dp,
                animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                label = "dotWidth_$index"
            )

            val dotColor by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "dotColor_$index"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .width(dotWidth)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}
