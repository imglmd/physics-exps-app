package com.imglmd.physicsexps.presentation.screens.constants

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
                    icon = R.drawable.math,
                    categoryLabel = "math_c",
                    listItem = listOf(
                        Item(
                            symbol = "π",
                            unit = "",
                            value = "3,141592654",
                            name = "pi"
                        ),
                        Item(
                            symbol = "e",
                            unit = "",
                            value = "2,718281828",
                            name = "e"
                        )
                    )
                )
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
                            name = "gravity"
                        ),
                        Item(
                            symbol = "G",
                            unit = "m_3_kg_s_2",
                            value = "6.67 × 10⁻¹¹",
                            name = "gravitational_constant"
                        ),
                        Item(
                            symbol = "M⊕",
                            unit = "kg",
                            value = "5,972 × 10²⁴",
                            name = "mass_earth"
                        ),
                        Item(
                            symbol = "R⊕",
                            unit = "m",
                            value = "6371",
                            name = "radius_earth"
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
                            value = "1.60218 × 10⁻¹⁹",
                            name = "el_ch"
                        ),
                        Item(
                            symbol = "mₑ",
                            unit = "kg",
                            value = "9.10938 × 10⁻³¹",
                            name = "electron_mass"
                        ),
                        Item(
                            symbol = "mₚ",
                            unit = "kg",
                            value = "1.67262 × 10⁻²⁷",
                            name = "proton_mass"
                        ),
                        Item(
                            symbol = "mₙ",
                            unit = "kg",
                            value = "1.67493 × 10⁻²⁷",
                            name = "neutron_mass"
                        ),
                        Item(
                            symbol = "k",
                            unit = "n_m_2_c_2",
                            value = "9 × 10⁹",
                            name = "coulomb_constant"
                        )
                    )
                )
            }
            item {
                CategoryItem(
                    categoryLabel = "phonics",
                    icon = R.drawable.sound,
                    listItem = listOf(
                        Item(
                            value = "331",
                            symbol = "c",
                            name = "speed_0",
                            unit = "m_s"
                        ),
                        Item(
                            value = "343",
                            symbol = "c",
                            name = "speed_20",
                            unit = "m_s"
                        ),
                        Item(
                            value = "1480",
                            unit = "m_s",
                            name = "speed_w",
                            symbol = "c"
                        ),
                        Item(
                            value = "5500",
                            unit = "m_s",
                            name = "speed_g",
                            symbol = "c"
                        ),
                        Item(
                            value = "5000 - 6100",
                            unit = "m_s",
                            name = "speed_s",
                            symbol = "c"
                        ),
                        Item(
                            value = "20 - 20000",
                            unit = "hz",
                            name = "human",
                            symbol = "f"
                        ),
                    )
                )
            }

            item {
                CategoryItem(
                    categoryLabel = "nuclear_physics",
                    icon = R.drawable.orbit,
                    listItem = listOf(
                        Item(
                            value = "1.66054 × 10⁻²⁷",
                            unit = "kg",
                            name = "u",
                            symbol = "u"
                        ),
                        Item(
                            value = "931.5",
                            unit = "mev",
                            name = "eq_atom",
                            symbol = "E"
                        ),
                        Item(
                            symbol = "mₑ",
                            unit = "kg",
                            value = "9.10938 × 10⁻³¹",
                            name = "electron_mass"
                        ),
                        Item(
                            symbol = "mₚ",
                            unit = "kg",
                            value = "1.67262 × 10⁻²⁷",
                            name = "proton_mass"
                        ),
                        Item(
                            symbol = "mₙ",
                            unit = "kg",
                            value = "1.67493 × 10⁻²⁷",
                            name = "neutron_mass"
                        ),
                    )
                )
            }

            item {
                CategoryItem(
                    icon = R.drawable.mol,
                    categoryLabel = "mol_ph",
                    listItem = listOf(
                        Item(
                            symbol = "k",
                            unit = "j_k",
                            value = "1.381 × 10⁻²³",
                            name = "b_const"
                        ),
                        Item(
                            symbol = "Nₐ",
                            unit = "mol_1",
                            value = "6.022 × 10²³",
                            name = "av_const"
                        ),
                        Item(
                            name = "un_gas",
                            symbol = "R",
                            unit = "j_mol_k",
                            value = "8.314"
                        ),
                        Item(
                            name = "mol_vol",
                            symbol = "Vₘ",
                            unit = "l_m",
                            value = "22.414"
                        ),
                        Item(
                            name = "atm_p",
                            symbol = "p₀",
                            unit = "pa",
                            value = "101325"
                        ),
                        Item(
                            name = "nor_t",
                            unit = "k",
                            symbol = "T₀",
                            value = "273.15"
                        )
                    )
                )
            }
        }
    }
}