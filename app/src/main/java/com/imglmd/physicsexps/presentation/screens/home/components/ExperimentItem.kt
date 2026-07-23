package com.imglmd.physicsexps.presentation.screens.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.core.ui.icons.AppIcons
import com.imglmd.physicsexps.domain.model.InputField

private val experimentImages = mapOf(
    "coulombs_law" to R.drawable.coulombs_law,
    "doppler_effect" to R.drawable.doppler,
    "free_fall" to R.drawable.freefall,
    "harmonic_vibrations" to R.drawable.harmonical,
    "joule_lenz" to R.drawable.joulelenz,
    "pendulum" to R.drawable.mathpendulum,
    "physical_pendulum" to R.drawable.physicalpendulum,
    "projectile_motion" to R.drawable.projectile_motion,
    "radioactive_decay" to R.drawable.radioactive_decay,
    "spring_pendulum" to R.drawable.springpendulum
)

@Composable
fun ExperimentItem(
    name: String,
    experimentId: String,
    inputs: List<InputField>,
    //placeholder: Int,
    //previewUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageRes = experimentImages[experimentId] ?: R.drawable.placeholder

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
            /*if (previewUrl != null) {
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
            }*/
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            BlueprintGrid(Modifier.matchParentSize())
        }
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, bottom = 4.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                inputs.forEach { input ->
                    InputSymbolBadge(symbol = input.symbol)
                }
            }
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = AppIcons.ChevronRight,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
        }
    }
}
@Composable
private fun InputSymbolBadge(symbol: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = symbol,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
