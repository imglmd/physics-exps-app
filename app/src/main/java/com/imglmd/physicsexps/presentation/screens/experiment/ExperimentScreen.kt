package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.components.AdvancedToggle
import com.imglmd.physicsexps.presentation.components.ExperimentAppBar
import com.imglmd.physicsexps.presentation.components.IconPosition
import com.imglmd.physicsexps.presentation.components.PrimaryButton
import com.imglmd.physicsexps.presentation.screens.experiment.components.ExperimentCarousel
import com.imglmd.physicsexps.presentation.screens.experiment.components.ExperimentTextField
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExperimentScreen(
    id: String,
    inputs: Map<String, String>?,
    replaceRunId: Int?,
    navigateBack: () -> Unit,
    navigateToResult: () -> Unit,
    viewModel: ExperimentViewModel = koinViewModel { parametersOf(id, inputs, replaceRunId) }
) {
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is ExperimentContract.Action.NavigateToResult -> {
                    keyboardController?.hide()
                    navigateToResult()
                }
                ExperimentContract.Action.NavigateBack -> {
                    keyboardController?.hide()
                    navigateBack()
                }
            }
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(state.error) {
        if (state.error != null) {
            listState.animateScrollToItem(
                index = listState.layoutInfo.totalItemsCount - 1,
                scrollOffset = -200
            )
        }
    }

    LaunchedEffect(state.isAdvancedMode) {
        if (state.isAdvancedMode) {
            kotlinx.coroutines.delay(200)

            listState.animateScrollToItem(
                index = listState.layoutInfo.totalItemsCount - 1,
                scrollOffset = -200

            )
        }
    }

    val textStates = remember(state.experiment.inputFields) {
        state.experiment.inputFields.associate { field ->
            field.key to TextFieldState(initialText = state.inputs[field.key] ?: "")
        }
    }
    state.experiment.inputFields.forEach { field ->
        val textState = textStates[field.key]!!
        LaunchedEffect(textState) {
            snapshotFlow { textState.text.toString() }
                .collect { value ->
                    viewModel.onIntent(
                        ExperimentContract.Intent.ChangeValue(
                            key = field.key,
                            newValue = value
                        )
                    )
                }
        }
    }

    val advancedTextStates = remember(state.experiment.additionalInputFields) {
        state.experiment.additionalInputFields.associate { field ->
            field.key to TextFieldState(initialText = state.inputs[field.key] ?: "")
        }
    }
    state.experiment.additionalInputFields.forEach { field ->
        val textState = advancedTextStates[field.key]!!
        LaunchedEffect(textState) {
            snapshotFlow { textState.text.toString() }
                .collect { value ->
                    viewModel.onIntent(ExperimentContract.Intent.ChangeValue(field.key, value))
                }
        }
    }

    Scaffold(
        topBar = {
            ExperimentAppBar(
                title = state.experiment.name,
                subtitle = state.experiment.category,
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ExperimentCarousel(
                        images = state.imageUrls,
                        isLoading = state.isImagesLoading
                    )
                }
                if (state.experiment.description.isNotEmpty()){
                item {
                    ExpandableDescription(
                        text = state.experiment.description
                    )
                }
                    }
                item {
                    Text(
                        text = "Введите известные величины",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        state.experiment.inputFields.forEachIndexed { index, field ->
                            val textState = textStates[field.key]!!
                            ExperimentTextField(
                                state = textState,
                                label = field.label,
                                symbol = field.symbol,
                                unit = field.unit,
                                modifier = Modifier.fillMaxWidth(),
                                isError = state.error != null,
                                isRequired = field.required
                            )
                        }
                        if (state.experiment.additionalInputFields.isNotEmpty() && state.isAdvancedMode) {
                            state.experiment.additionalInputFields.forEach { field ->
                                val textState = advancedTextStates[field.key]!!
                                ExperimentTextField(
                                    state = textState,
                                    label = field.label,
                                    symbol = field.symbol,
                                    unit = field.unit,
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = state.error != null
                                )
                            }
                        }
                    }
                }

                if (state.experiment.additionalInputFields.isNotEmpty()){
                    item {
                        AdvancedToggle(
                            title = "Расширенный режим",
                            subtitle = if (state.isAdvancedMode) "Доп. параметры включены" else "Только основные параметры",
                            icon = ImageVector.vectorResource(R.drawable.rocket),
                            enabled = state.isAdvancedMode,
                            onToggle = {
                                viewModel.onIntent(ExperimentContract.Intent.ToggleAdvancedMode)
                            }
                        )
                    }
                }

                state.error?.let { error ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.error),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }

            }

            val imeVisible = WindowInsets.isImeVisible

            AnimatedVisibility(
                visible = !imeVisible || state.isButtonActive,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                PrimaryButton(
                    text = "Начать эксперимент",
                    isLoading = state.isLoading,
                    enabled = state.isButtonActive,
                    onClick = { viewModel.onIntent(ExperimentContract.Intent.Start) },
                    iconPosition = IconPosition.EdgeEnd,
                    icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .navigationBarsPadding()
                        .imePadding()
                )
            }
        }
    }
}


@Composable
fun ExpandableDescription(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3
) {
    var expanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .then(
                if (isOverflowing) {
                    Modifier.clickable { expanded = !expanded }
                } else Modifier
            )
            .padding(16.dp)
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (!expanded) {
                    isOverflowing = result.hasVisualOverflow
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp)
        )

        AnimatedVisibility(
            visible = isOverflowing,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,
                label = "arrow_rotation"
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }
    }
}
