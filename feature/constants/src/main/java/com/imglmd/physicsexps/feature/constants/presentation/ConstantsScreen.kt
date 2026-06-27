package com.imglmd.physicsexps.feature.constants.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Category
import com.imglmd.physicsexps.feature.constants.presentation.component.CategoryItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConstantsScreen(
    viewModel: ConstantsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val finalFiltered = mutableListOf<Category>()
    state.allCategories.forEach { cat ->
        val catName = getStringByKey(cat.label)
        val search = state.search.trim()
        val isShow = catName.contains(search, ignoreCase = true)
        if (isShow) {
            finalFiltered.add(cat)
        }
    }

    val searchState = rememberTextFieldState()

    LaunchedEffect(searchState.text) {
        viewModel.onIntent(ConstantsContract.Intent.ChangeSearchText(searchState.text.toString()))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 110.dp,
                start = 16.dp,
                end = 16.dp
            )
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.constants),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                SearchTextField(
                    state = searchState
                )
            }
            finalFiltered.forEach { category ->
                item {
                    CategoryItem(
                        categoryLabel = category.label,
                        listItem = category.listItem,
                        icon = category.icon
                    )
                }
            }
        }
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
            imageVector = ImageVector.vectorResource(R.drawable.search),
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

                Box(
                    contentAlignment = Alignment.CenterStart
                ) {

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
            IconButton(
                onClick = { state.clearText() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cross),
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
