@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.imglmd.physicsexps.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.imglmd.physicsexps.BuildConfig
import com.imglmd.physicsexps.feature.constants.presentation.ConstantsScreen
import com.imglmd.physicsexps.feature.settings.presentation.SettingsScreen
import com.imglmd.physicsexps.presentation.core.getStringByKey
import com.imglmd.physicsexps.presentation.navigation.Screen
import com.imglmd.physicsexps.presentation.screens.home.HomeScreen
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@Composable
fun TabHostScreen(
    navigateToExperiment: (String) -> Unit,
    navigateToResult: (Int) -> Unit,
    navigateToHistory: () -> Unit,
) {
    var currentTab by rememberSaveable { mutableIntStateOf(Screen.Tab.Home.ordinal) }
    val homeGridState = rememberLazyGridState()

    val tabs = Screen.Tab.entries
    val pagerState = rememberPagerState(
        initialPage = currentTab,
        pageCount = { tabs.size },
    )

    val scope = rememberCoroutineScope()

    val bottomBarVisible by remember {
        derivedStateOf {
            if (tabs[currentTab] != Screen.Tab.Home)  true
            else homeGridState.firstVisibleItemIndex == 0 && homeGridState.firstVisibleItemScrollOffset < 20
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        currentTab = pagerState.currentPage
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
                Screen.Tab.Constants -> ConstantsScreen()
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
                tabs = tabs,
                pagerState = pagerState,
                currentTab = currentTab,
                onTabChange = { page ->
                    currentTab = page
                    scope.launch { pagerState.animateScrollToPage(page) }
                }
            )
        }
    }
}

@Composable
private fun TabHostBottomBar(
    tabs: EnumEntries<Screen.Tab>,
    pagerState: PagerState,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
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
                .padding(bottom = 6.dp)
                .navigationBarsPadding()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
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
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(gapDp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEachIndexed { index, tab ->
                        BottomBarButton(
                            selected = currentTab == index,
                            selectedIcon = tab.selectedIcon,
                            unselectedIcon = tab.unselectedIcon,
                            label = getStringByKey(tab.label),
                            width = buttonWidthDp,
                            onClick = { onTabChange(index) },
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

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier.height(46.dp).width(width)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = CircleShape,
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}