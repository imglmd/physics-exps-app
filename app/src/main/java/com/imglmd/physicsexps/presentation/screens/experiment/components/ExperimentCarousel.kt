package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentCarousel(
    images: List<String>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val imageCount = if (isLoading) 2 else images.size
    val carouselState = rememberCarouselState {
        imageCount
    }

    var selectedIndex by remember {
        mutableStateOf<Int?>(null)
    }

    AnimatedVisibility(
        visible = isLoading || images.isNotEmpty(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {

        Column(
            modifier = modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HorizontalMultiBrowseCarousel(
                state = carouselState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                preferredItemWidth = 220.dp,
                itemSpacing = 8.dp
            ) { index ->
                ExperimentCarouselItem(
                    imageUrl = images.getOrNull(index),
                    isLoading = isLoading,
                    modifier = Modifier.maskClip(
                        RoundedCornerShape(20.dp)
                    ),
                    onClick = { selectedIndex = index }
                )
            }

            CarouselIndicators(
                count = imageCount,
                current = carouselState.currentItem
            )
        }
    }
    selectedIndex?.let { index ->
        ImageViewer(
            images = images,
            startIndex = index,
            onDismiss = { selectedIndex = null }
        )
    }
}
