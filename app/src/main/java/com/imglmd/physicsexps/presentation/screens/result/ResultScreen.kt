@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.imglmd.physicsexps.presentation.screens.result

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.experiments.ExperimentRegistry
import com.imglmd.physicsexps.core.ui.component.ExperimentAppBar
import com.imglmd.physicsexps.presentation.screens.result.components.ChartCard
import com.imglmd.physicsexps.presentation.screens.result.components.CommentSection
import com.imglmd.physicsexps.presentation.screens.result.components.MediaSection
import com.imglmd.physicsexps.presentation.screens.result.components.ResultCard
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.core.net.toUri
import com.imglmd.physicsexps.core.ui.haptic.LocalHapticManager
import com.imglmd.physicsexps.presentation.core.getStringByKey

@Composable
fun ResultScreen(
    runId: Int?,
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    navigateChart: (Int) -> Unit,
    navigateCompare: (Int) -> Unit,
    navigateExperiment: (String, Map<String, String>, Int?) -> Unit,
    navigateSolution: () -> Unit,
    viewModel: ResultViewModel = koinViewModel { parametersOf(runId) }
) {
    val state by viewModel.state.collectAsState()
    val registry = koinInject<ExperimentRegistry>()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ResultContract.Effect.NavigateBack -> navigateBack()
                ResultContract.Effect.NavigateHome -> navigateHome()
                is ResultContract.Effect.NavigateExperiment ->
                    navigateExperiment(effect.id, effect.inputs, effect.replaceRunId)
                is ResultContract.Effect.NavigateChart ->
                    navigateChart(effect.runId)
                ResultContract.Effect.NavigateSolution -> navigateSolution()
                is ResultContract.Effect.NavigateCompare -> navigateCompare(effect.runId)
            }
        }
    }

    when (val s = state) {
        ResultContract.State.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ResultContract.State.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = s.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        is ResultContract.State.Success -> {
            Content(
                state = s,
                registry = registry,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
private fun Content(
    state: ResultContract.State.Success,
    registry: ExperimentRegistry,
    onIntent: (ResultContract.Intent) -> Unit
) {
    val experiment = remember(state.result.experimentId) {
        registry.getById(state.result.experimentId)
    }

    val hasSolution = remember(state.result.experimentId) {
        registry.getById(state.result.experimentId)
            .getSolutionSteps(null)
            .isNotEmpty()
    }

    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberScrollState()
    val expName = getStringByKey(state.result.experimentId)
    val section = getStringByKey(state.result.experimentId)

    BackHandler { onIntent(ResultContract.Intent.Back) }

    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = getStringByKey(experiment.id),
                subtitle = getStringByKey(experiment.category),
                navigateBack = { onIntent(ResultContract.Intent.Back) }
            )
        },
        bottomBar = {
            BottomActions(
                onDelete = { onIntent(ResultContract.Intent.Delete) },
                onSave = { onIntent(ResultContract.Intent.Save) },
                onCompare = { onIntent(ResultContract.Intent.Compare) },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            Spacer(Modifier.height(16.dp))
            val data: Map<String, String> =
                state.result.quantities.associate { getStringByKey(it.label) to "${"%.3f".format(it.value)} ${getStringByKey(it.unit)}"
                }

            ResultCard(
                state = state,
                hasSolution = hasSolution,
                navigateSolution = { onIntent(ResultContract.Intent.OpenSolution) },
                onChangeClick = { onIntent(ResultContract.Intent.Change) },
                onPdfClick = {
                    saveResultAsPdf(
                        context = context,
                        nameExp = expName,
                        nameSection = section,
                        data = data
                    )
                }
            )

            if (state.result.points.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                ChartCard(
                    points = state.result.points,
                    xLabel = state.result.xLabel,
                    yLabel = state.result.yLabel,
                    modelProducer = modelProducer,
                    onChartClick = { onIntent(ResultContract.Intent.OpenChart) }
                )
            }

            if (!state.onlineState.offlineMode){
                Spacer(Modifier.height(20.dp))

                MediaSection(
                    media = state.media,
                    onlineState = state.onlineState,
                    isLoading = state.isMediaLoading,
                    isUploading = state.isMediaUploading,
                    errorMessage = state.mediaErrorMessage,
                    onUpload = { onIntent(ResultContract.Intent.UploadMedia(it)) },
                    onDelete = { onIntent(ResultContract.Intent.DeleteMedia(it)) },
                    onRefresh = { onIntent(ResultContract.Intent.RefreshMedia) },
                    onOpen = { url -> openMedia(context, url) }
                )
            }

            Spacer(Modifier.height(20.dp))

            CommentSection(
                comments = state.comments,
                onAddComment = { onIntent(ResultContract.Intent.AddComment(it)) },
                onClickDelete = { onIntent(ResultContract.Intent.DeleteComment(it)) }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BottomActions(
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onCompare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.67f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.92f)
                    )
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

        HorizontalFloatingToolbar(
            colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                toolbarContainerColor = MaterialTheme.colorScheme.surface,

            ),
            expanded = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
                .border(1.dp,MaterialTheme.colorScheme.outlineVariant, CircleShape),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                ToolbarButton(
                    icon = Icons.Outlined.Delete,
                    contentDescription = "Удалить",
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )

                ToolbarButton(
                    icon = ImageVector.vectorResource(R.drawable.compare_arrows),
                    contentDescription = "Сравнить",
                    onClick = onCompare,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )

                ToolbarButton(
                    icon = ImageVector.vectorResource(R.drawable.save),
                    contentDescription = "Сохранить",
                    onClick = onSave,
                    modifier = Modifier.weight(2f),
                    iconPosition = ToolbarIconPosition.RIGHT,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

            }
        }
    }
}

private fun openMedia(context: Context, url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

private enum class ToolbarIconPosition{ LEFT, RIGHT }
@Composable
private fun ToolbarButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    text: String? = null,
    iconPosition: ToolbarIconPosition = ToolbarIconPosition.LEFT
) {

    val interactionSource = remember { MutableInteractionSource() }

    val haptic = LocalHapticManager.current

    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedCorner by animateDpAsState(
        targetValue = if (isPressed) 22.dp else 50.dp,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "corner"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.03f else 1f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "scale"
    )

    val hasText = !text.isNullOrBlank()
    val iconSize = if(hasText) 20.dp else 24.dp

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.perform(HapticFeedbackType.Confirm)
        }
    }

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .height(52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale },
        shape = RoundedCornerShape(animatedCorner),
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (iconPosition == ToolbarIconPosition.LEFT) {

                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )

                if (hasText) Spacer(Modifier.width(8.dp))
            }

            if (hasText) {
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (iconPosition == ToolbarIconPosition.RIGHT) {

                if (hasText) Spacer(Modifier.width(8.dp))

                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
