package com.imglmd.physicsexps.feature.constants.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.core.ui.RadioGroup
import com.imglmd.physicsexps.core.ui.RadioOption
import com.imglmd.physicsexps.core.ui.preferences.PreferenceGroup
import com.imglmd.physicsexps.core.ui.preferences.PreferenceSlider
import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Category
import com.imglmd.physicsexps.feature.constants.domain.model.Constant
import com.imglmd.physicsexps.feature.constants.presentation.component.CategoryItem
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConstantsScreen(
    viewModel: ConstantsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val query = state.search.trim()

    val visibleCategories: List<Category> = if (query.isBlank()) {
        state.allCategories
    } else {
        state.allCategories.mapNotNull { category -> filterCategory(category, query) }
    }

    val searchState = rememberTextFieldState()
    var settingsExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(searchState.text) {
        viewModel.onIntent(ConstantsContract.Intent.ChangeSearchText(searchState.text.toString()))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())
        ) {
            ScreenHeader(
                settingsExpanded = settingsExpanded,
                onSettingsClick = {
                    settingsExpanded = !settingsExpanded
                    if (settingsExpanded) scope.launch { listState.animateScrollToItem(0) }
                }
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).clip(RoundedCornerShape(24.dp)),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 110.dp
                )
            ) {
                item {
                    SearchTextField(state = searchState)
                    AnimatedVisibility(
                        modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                        visible = settingsExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(Modifier.height(24.dp))
                            SettingsSection(
                                digits = state.preferences.digits,
                                copyMode = state.preferences.copyMode,
                                onDigitsChange = { viewModel.onIntent(ConstantsContract.Intent.ChangeDigits(it)) },
                                onCopyModeChange = { viewModel.onIntent(ConstantsContract.Intent.ChangeCopyMode(it)) }
                            )
                        }
                    }
                }

                if (visibleCategories.isEmpty()) {
                    item { EmptySearchState() }
                } else {
                    items(visibleCategories, key = { it.labelRes }) { category ->
                        CategoryItem(
                            labelRes = category.labelRes,
                            preferences = state.preferences,
                            listConstant = category.listConstant,
                            icon = category.icon
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    settingsExpanded: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(48.dp))

        Text(
            text = stringResource(R.string.constants),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = ImageVector.vectorResource(com.imglmd.physicsexps.core.ui.R.drawable.tune),
                contentDescription = stringResource(R.string.display_preferences),
                tint = if (settingsExpanded) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    digits: Int,
    copyMode: CopyMode,
    onDigitsChange: (Int) -> Unit,
    onCopyModeChange: (CopyMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PreferenceGroup(stringResource(R.string.display_preferences)) {
            PreferenceSlider(
                title = stringResource(R.string.digits),
                subtitle = stringResource(R.string.digits_subtitle),
                value = digits,
                values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                valueLabel = { it.toString() },
                onValueChange = onDigitsChange
            )
        }

        PreferenceGroup(stringResource(R.string.copy_mode_title)) {
            RadioGroup(
                options = listOf(
                    RadioOption(
                        value = CopyMode.VALUE,
                        title = stringResource(R.string.copy_mode_value),
                        subtitle = stringResource(R.string.copy_mode_value_example)
                    ),
                    RadioOption(
                        value = CopyMode.SYMBOL_VALUE,
                        title = stringResource(R.string.copy_mode_symbol_value),
                        subtitle = stringResource(R.string.copy_mode_symbol_value_example)
                    ),
                    RadioOption(
                        value = CopyMode.FULL,
                        title = stringResource(R.string.copy_mode_full),
                        subtitle = stringResource(R.string.copy_mode_full_example)
                    )
                ),
                selected = copyMode,
                onSelected = onCopyModeChange
            )
        }
    }
}

@Composable
private fun filterCategory(category: Category, query: String): Category? {
    val label = stringResource(category.labelRes)
    if (label.contains(query, ignoreCase = true)) return category

    val matchingItems = category.listConstant.filter { item -> itemMatches(item, query) }
    return if (matchingItems.isEmpty()) null else category.copy(listConstant = matchingItems)
}

@Composable
private fun itemMatches(constant: Constant, query: String): Boolean {
    val name = stringResource(constant.nameRes)
    return constant.symbol.contains(query, ignoreCase = true) ||
            name.contains(query, ignoreCase = true)
}

@Composable
private fun EmptySearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.constants_nothing_found),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.bodyLarge

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(100)
            )
            .clip(RoundedCornerShape(100))
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 16.dp, end = 4.dp)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(com.imglmd.physicsexps.core.ui.R.drawable.search),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            state = state,
            textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.weight(1f),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (state.text.isNotEmpty()) {
            IconButton(onClick = { state.clearText() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cross),
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}