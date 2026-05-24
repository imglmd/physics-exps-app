package com.imglmd.physicsexps.presentation.screens.experiment.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.imglmd.physicsexps.presentation.rememberShimmerBrush

@Composable
fun ExperimentCarouselItem(
    imageUrl: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var imageLoading by remember(imageUrl) {
        mutableStateOf(imageUrl != null)
    }

    val imageAlpha by animateFloatAsState(
        targetValue = if (imageLoading) 0f else 1f,
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
        label = "imageAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        AnimatedVisibility(
            visible = isLoading || imageLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ImageShimmer(modifier = Modifier.fillMaxSize())
        }

        if (!isLoading && imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = imageAlpha },
                contentScale = ContentScale.Crop,
                onLoading = { imageLoading = true },
                onSuccess = { imageLoading = false },
                onError = { imageLoading = false }
            )
        }
    }
}

@Composable
private fun ImageShimmer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(rememberShimmerBrush())
    )
}
