package com.imglmd.physicsexps.presentation.screens.constants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.presentation.core.getStringByKey
import com.imglmd.physicsexps.presentation.screens.constants.component.CategoryItem
import com.imglmd.physicsexps.presentation.screens.constants.component.Item

@Composable
fun ConstantsScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 110.dp,
                start = 16.dp,
                end = 16.dp
            ),
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
                CategoryItem(
                    icon = R.drawable.mechenics_icon,
                    categoryLabel = "mechanics",
                    listItem = listOf(
                        Item(
                            symbol = "g",
                            unit = "m_s_2",
                            value = "9.80665",
                            name = getStringByKey("gravity")
                        ),
                        Item(
                            symbol = "G",
                            unit = "m_3_kg_s_2",
                            value = "6.67 × 10⁻¹¹",
                            name = getStringByKey("gravitational_constant")
                        ),
                        Item(
                            symbol = "M⊕",
                            unit = "kg",
                            value = "5,972 × 10²⁴",
                            name = getStringByKey("mass_earth")
                        ),
                        Item(
                            symbol = "R⊕",
                            unit = "m",
                            value = "6371",
                            name = getStringByKey("radius_earth")
                        )
                    )
                )
            }

            item {
                CategoryItem(
                    icon = R.drawable.light,
                    categoryLabel = "electricity",
                    listItem = listOf(
                        Item(
                            symbol = "e",
                            unit = "c",
                            value = "1.60217 × 10⁻¹⁹",
                            name = getStringByKey("el_ch")
                        )
                    )
                )
            }
        }
    }
}