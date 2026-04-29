package com.imglmd.physicsexps.presentation.screens.solution

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.LatexAutoWrap
import com.hrm.latex.renderer.model.LatexConfig
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.SolutionStep
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SolutionScreen(
    navigateBack: () -> Unit,
    viewModel: SolutionViewModel = koinViewModel()
) {
    val steps = viewModel.solutionSteps.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.goBackFlow.collect {
            navigateBack()
        }
    }

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible == layoutInfo.totalItemsCount - 1
        }
    }

    val width by animateDpAsState(
        targetValue = if (isAtBottom) 360.dp else 56.dp,
        label = ""
    )

    val height by animateDpAsState(
        targetValue = if (isAtBottom) 56.dp else 56.dp,
        label = ""
    )

    val corner by animateDpAsState(
        targetValue = if (isAtBottom) 16.dp else 28.dp,
        label = ""
    )

    Scaffold() { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                itemsIndexed(steps.value) { index, step ->
                    SolutionStepCard(index = index + 1, step = step)
                }
                item {
                    Spacer(Modifier.height(60.dp))
                }
                item {
                }
            }
            Surface(
                onClick = navigateBack,
                shape = RoundedCornerShape(corner),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding.calculateBottomPadding() + 4.dp)
                    .size(width, height)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = if (isAtBottom) 16.dp else 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    AnimatedVisibility(
                        visible = isAtBottom,
                        enter = fadeIn(animationSpec = tween(200)) +
                                slideInHorizontally(
                                    animationSpec = tween(200),
                                    initialOffsetX = { it / 2 }
                                ),
                        exit = fadeOut(animationSpec = tween(100))
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(8.dp))

                            Text(
                                "Вернуться назад",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun SolutionStepCard(index: Int, step: SolutionStep) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp, top = 16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = index.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ) {

            when (step) {

                is SolutionStep.Theory -> {
                    StepTitle(step.title, Icons.Default.Info)
                    Spacer(Modifier.height(8.dp))

                    Text(
                        step.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is SolutionStep.Formula -> {
                    StepTitle(step.description, ImageVector.vectorResource(R.drawable.science))
                    Spacer(Modifier.height(12.dp))

                    FormulaBox(step.expression)
                }

                is SolutionStep.Substitution -> {
                    StepTitle(step.description, ImageVector.vectorResource(R.drawable.bolt))

                    Spacer(Modifier.height(8.dp))

                    FormulaBox(step.expression)

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        LatexAutoWrap(
                            latex = step.result,
                            modifier = Modifier.padding(12.dp),
                            config = LatexConfig(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                darkColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                is SolutionStep.Result -> {
                    StepTitle("Ответ", Icons.Default.CheckCircle)

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            step.quantities.forEach { q ->

                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {

                                    Text(
                                        q.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Latex(
                                        latex = "${q.symbol} = ${"%.3g".format(q.value)} \\text{${q.unit}}",
                                        config = LatexConfig(
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            darkColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulaBox(expression: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
    ) {

        LatexAutoWrap(
            latex = expression,
            modifier = Modifier.padding(12.dp),
            config = LatexConfig(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                darkColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
private fun StepTitle(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}