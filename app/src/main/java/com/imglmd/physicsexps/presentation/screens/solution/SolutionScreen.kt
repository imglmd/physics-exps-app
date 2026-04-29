package com.imglmd.physicsexps.presentation.screens.solution

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.graphicsLayer
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


data class StepFocusState(
    val isActive: Boolean,
    val isPast: Boolean,
    val hasFocus: Boolean,
    val isEnd: Boolean
)

@Composable
fun SolutionScreen(
    navigateBack: () -> Unit,
    viewModel: SolutionViewModel = koinViewModel()
) {
    val steps by viewModel.solutionSteps.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.goBackFlow.collect { navigateBack() }
    }

    val activeIndex by remember {
        derivedStateOf {

            if (!listState.canScrollForward || !listState.canScrollBackward) {
                return@derivedStateOf -1
            }

            val layoutInfo = listState.layoutInfo
            val stepItems = layoutInfo.visibleItemsInfo.filter { it.index < steps.size }

            if (stepItems.isEmpty()) return@derivedStateOf 0

            val threshold = layoutInfo.viewportSize.height * 0.40f

            (stepItems
                .filter { it.offset <= threshold }
                .maxByOrNull { it.index }
                ?: stepItems.first()
                    ).index
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
        animationSpec = tween(300),
        label = "fab_width"
    )
    val corner by animateDpAsState(
        targetValue = if (isAtBottom) 16.dp else 28.dp,
        animationSpec = tween(300),
        label = "fab_corner"
    )

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 80.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                itemsIndexed(steps) { index, step ->
                    val hasActive = activeIndex != -1

                    val focusState = StepFocusState(
                        isActive = hasActive && index == activeIndex,
                        isPast = hasActive && index < activeIndex || !listState.canScrollForward,
                        hasFocus = hasActive,
                        isEnd = isAtBottom
                    )
                    SolutionStepCard(
                        index = index + 1,
                        step = step,
                        focusState = focusState,
                        isLast = index == steps.lastIndex
                    )
                }
                item { }
            }

            Surface(
                onClick = navigateBack,
                shape = RoundedCornerShape(corner),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding.calculateBottomPadding() + 4.dp)
                    .size(width, 56.dp)
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
                        enter = fadeIn(tween(200)) + slideInHorizontally(tween(200)) { it / 2 },
                        exit = fadeOut(tween(100))
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