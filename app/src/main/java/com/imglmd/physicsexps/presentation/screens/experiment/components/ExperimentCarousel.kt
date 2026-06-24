package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentCarousel(
    images: List<String>,
    isLoading: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val imageCount = if (isLoading) 2 else images.size
    val carouselState = rememberCarouselState { imageCount }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    AnimatedVisibility(
        visible = isLoading || images.isNotEmpty() || isError,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (isError) {
                ImagesErrorPlaceholder(modifier = Modifier.fillMaxWidth())
            } else {
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
                        modifier = Modifier.maskClip(RoundedCornerShape(20.dp)),
                        onClick = { selectedIndex = index }
                    )
                }

                CarouselIndicators(
                    count = imageCount,
                    current = carouselState.currentItem
                )
            }
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

@Composable
private fun ImagesErrorPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "Не удалось загрузить изображения",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}