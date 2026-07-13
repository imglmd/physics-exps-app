package com.imglmd.physicsexps.presentation.screens.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun ExperimentItem(
    name: String,
    placeholder: Int,
    previewUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
               RoundedCornerShape(24.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.067f)
                .clip(RoundedCornerShape(19.dp))
        ) {
            if (previewUrl != null) {
                AsyncImage(
                    model = previewUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(placeholder),
                    error = painterResource(placeholder),
                    fallback = painterResource(placeholder)
                )
            } else {
                Image(
                    painter = painterResource(placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            BlueprintGrid(Modifier.matchParentSize())
        }
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
        )
    }
}

//TODO сделатб красивее
@Composable
private fun BlueprintGrid(
    modifier: Modifier = Modifier
) {
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Canvas(modifier = modifier) {

        val verticalCells = 6
        val horizontalCells = 6

        val stepX = size.width / verticalCells
        val stepY = size.height / horizontalCells

        for (i in 1 until horizontalCells) {
            drawLine(
                color = gridColor,
                start = Offset(0f, stepY * i),
                end = Offset(size.width, stepY * i),
                strokeWidth = 1.dp.toPx()
            )
        }

        for (i in 1 until verticalCells) {
            drawLine(
                color = gridColor,
                start = Offset(stepX * i, 0f),
                end = Offset(stepX * i, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
