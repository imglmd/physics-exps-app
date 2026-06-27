package com.imglmd.physicsexps.feature.constants.domain.usecase

import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Category
import com.imglmd.physicsexps.feature.constants.domain.model.Item

// TODO: вынести данные в data
class GetAllCategoriesUseCase(
) {
    operator fun invoke(): List<Category> {
        return listOf(
            Category(
                icon = R.drawable.math,
                label = "math_c",
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
            ),
            Category(
                icon = R.drawable.mechenics_icon,
                label = "mechanics",
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
            ),


            Category(
                icon = R.drawable.light,
                label = "electricity",
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
            ),

            Category(
                label = "phonics",
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
            ),

            Category(
                label = "nuclear_physics",
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
            ),

            Category(
                icon = R.drawable.mol,
                label = "mol_ph",
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
        )
    }
}