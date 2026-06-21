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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        HorizontalFloatingToolbar(
            expanded = true,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .navigationBarsPadding(),
        ) {
            val buttonWidth = 104.dp
            val buttonGap = 8.dp
            val stepPx = with(LocalDensity.current) { (buttonWidth + buttonGap).toPx() }

            val pillOffsetPx by remember {
                derivedStateOf {
                    (pagerState.currentPage + pagerState.currentPageOffsetFraction) * stepPx
                }
            }

            Box {
                Box(
                    modifier = Modifier
                        .size(width = buttonWidth, height = 52.dp)
                        .graphicsLayer { translationX = pillOffsetPx }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            shape = CircleShape,
                        )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(buttonGap)) {
                    Screen.Tab.entries.forEach { tab ->
                        BottomBarButton(
                            selected = currentTab == tab,
                            selectedIcon = tab.selectedIcon,
                            unselectedIcon = tab.unselectedIcon,
                            contentDescription = tab.name,
                            width = buttonWidth,
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
    contentDescription: String,
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
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(200),
        label = "icon_color",
    )

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .height(52.dp)
            .width(width)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = CircleShape,
        color = Color.Transparent,
        contentColor = iconColor,
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = contentDescription,
                modifier = Modifier.size(26.dp),
            )
        }
    }
}