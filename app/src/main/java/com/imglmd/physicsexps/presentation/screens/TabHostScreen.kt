@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.imglmd.physicsexps.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imglmd.physicsexps.BuildConfig
import com.imglmd.physicsexps.feature.settings.ui.SettingsScreen
import com.imglmd.physicsexps.presentation.navigation.Navigator
import com.imglmd.physicsexps.presentation.navigation.Screen
import com.imglmd.physicsexps.presentation.screens.home.HomeScreen
import org.koin.compose.koinInject

@Composable
fun TabHostScreen(
    navigateToExperiment: (String) -> Unit,
    navigateToResult: (Int) -> Unit,
    navigateToHistory: () -> Unit,
) {
    val navigator = koinInject<Navigator>()
    val currentTab by navigator.currentTab.collectAsStateWithLifecycle()
    val homeGridState = rememberLazyGridState()

    BackHandler {
        navigator.switchTab(Screen.Tab.Home)
    }

    val tabs = Screen.Tab.entries
    val pagerState = rememberPagerState(
        initialPage = currentTab.ordinal,
        pageCount = { tabs.size },
    )

    val bottomBarVisible by remember {
        derivedStateOf {
            if (currentTab != Screen.Tab.Home)  true
            else homeGridState.firstVisibleItemIndex == 0 && homeGridState.firstVisibleItemScrollOffset < 20
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        val swipedTab = tabs[pagerState.settledPage]
        if (swipedTab != currentTab) navigator.switchTab(swipedTab)
    }

    LaunchedEffect(currentTab) {
        val targetPage = currentTab.ordinal
        if (pagerState.settledPage != targetPage) pagerState.animateScrollToPage(targetPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true,
            beyondViewportPageCount = 1,
        ) { page ->
            when (tabs[page]) {
                Screen.Tab.Home -> HomeScreen(
                    navigateToExperiment = navigateToExperiment,
                    navigateToResult = navigateToResult,
                    navigateToHistory = navigateToHistory,
                    gridState = homeGridState
                )
                Screen.Tab.Settings -> SettingsScreen(versionName = BuildConfig.VERSION_NAME)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.67f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                            Color.Transparent,
                        )
                    )
                )
        )

        AnimatedVisibility(
            visible = bottomBarVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            TabHostBottomBar(
                pagerState = pagerState,
                currentTab = currentTab,
                onTabChange = { navigator.switchTab(it) }
            )
        }
    }
}

@Composable
private fun TabHostBottomBar(
    pagerState: PagerState,
    currentTab: Screen.Tab,
    onTabChange: (Screen.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.42f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.67f),
                    )
                )
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .navigationBarsPadding()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val density = LocalDensity.current

            val buttonWidthDp = 86.dp
            val gapDp = 4.dp
            val stepPx = with(density) { (buttonWidthDp + gapDp).toPx() }
            val pillOffsetPx by remember {
                derivedStateOf {
                    (pagerState.currentPage + pagerState.currentPageOffsetFraction) * stepPx
                }
            }

            Box {
                Box(
                    modifier = Modifier
                        .size(width = buttonWidthDp, height = 46.dp)
                        .graphicsLayer { translationX = pillOffsetPx }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))

                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(gapDp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Screen.Tab.entries.forEach { tab ->
                        BottomBarButton(
                            selected = currentTab == tab,
                            selectedIcon = tab.selectedIcon,
                            unselectedIcon = tab.unselectedIcon,
                            label = tab.label,
                            width = buttonWidthDp,
                            onClick = { onTabChange(tab) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBarButton(
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    label: String,
    width: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "scale",
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "icon_color",
    )

    val labelColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "label_color",
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier.height(46.dp).width(width)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = CircleShape,
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = labelColor,
                letterSpacing = 0.15.sp,
                lineHeight = 12.sp,
                maxLines = 1
            )
        }
    }
}