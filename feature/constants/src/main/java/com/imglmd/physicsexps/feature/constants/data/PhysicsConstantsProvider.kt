package com.imglmd.physicsexps.feature.constants.data

import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Category
import com.imglmd.physicsexps.feature.constants.domain.model.Item

internal object PhysicsConstantsProvider {

    fun provide(): List<Category> = listOf(
        Category(
            icon = R.drawable.math,
            labelRes = R.string.math_c,
            listItem = listOf(
                Item(symbol = "π", value = "3,141592654", nameRes = R.string.pi),
                Item(symbol = "e", value = "2,718281828", nameRes = R.string.e)
            )
        ),
        Category(
            icon = R.drawable.mechenics_icon,
            labelRes = R.string.mechanics,
            listItem = listOf(
                Item(symbol = "g", unitRes = R.string.m_s_2, value = "9.80665", nameRes = R.string.gravity),
                Item(symbol = "G", unitRes = R.string.m_3_kg_s_2, value = "6.67 × 10⁻¹¹", nameRes = R.string.gravitational_constant),
                Item(symbol = "M⊕", unitRes = R.string.kg, value = "5,972 × 10²⁴", nameRes = R.string.mass_earth),
                Item(symbol = "R⊕", unitRes = R.string.m, value = "6371", nameRes = R.string.radius_earth)
            )
        ),
        Category(
            icon = R.drawable.light,
            labelRes = R.string.electricity,
            listItem = listOf(
                Item(symbol = "e", unitRes = R.string.c, value = "1.60218 × 10⁻¹⁹", nameRes = R.string.el_ch),
                Item(symbol = "mₑ", unitRes = R.string.kg, value = "9.10938 × 10⁻³¹", nameRes = R.string.electron_mass),
                Item(symbol = "mₚ", unitRes = R.string.kg, value = "1.67262 × 10⁻²⁷", nameRes = R.string.proton_mass),
                Item(symbol = "mₙ", unitRes = R.string.kg, value = "1.67493 × 10⁻²⁷", nameRes = R.string.neutron_mass),
                Item(symbol = "k", unitRes = R.string.n_m_2_c_2, value = "9 × 10⁹", nameRes = R.string.coulomb_constant)
            )
        ),
        Category(
            icon = R.drawable.sound,
            labelRes = R.string.phonics,
            listItem = listOf(
                Item(symbol = "c", unitRes = R.string.m_s, value = "331", nameRes = R.string.speed_0),
                Item(symbol = "c", unitRes = R.string.m_s, value = "343", nameRes = R.string.speed_20),
                Item(symbol = "c", unitRes = R.string.m_s, value = "1480", nameRes = R.string.speed_w),
                Item(symbol = "c", unitRes = R.string.m_s, value = "5500", nameRes = R.string.speed_g),
                Item(symbol = "c", unitRes = R.string.m_s, value = "5000 - 6100", nameRes = R.string.speed_s),
                Item(symbol = "f", unitRes = R.string.hz, value = "20 - 20000", nameRes = R.string.human)
            )
        ),
        Category(
            icon = R.drawable.orbit,
            labelRes = R.string.nuclear_physics,
            listItem = listOf(
                Item(symbol = "u", unitRes = R.string.kg, value = "1.66054 × 10⁻²⁷", nameRes = R.string.u),
                Item(symbol = "E", unitRes = R.string.mev, value = "931.5", nameRes = R.string.eq_atom),
                Item(symbol = "mₑ", unitRes = R.string.kg, value = "9.10938 × 10⁻³¹", nameRes = R.string.electron_mass),
                Item(symbol = "mₚ", unitRes = R.string.kg, value = "1.67262 × 10⁻²⁷", nameRes = R.string.proton_mass),
                Item(symbol = "mₙ", unitRes = R.string.kg, value = "1.67493 × 10⁻²⁷", nameRes = R.string.neutron_mass)
            )
        ),
        Category(
            icon = R.drawable.mol,
            labelRes = R.string.mol_ph,
            listItem = listOf(
                Item(symbol = "k", unitRes = R.string.j_k, value = "1.381 × 10⁻²³", nameRes = R.string.b_const),
                Item(symbol = "Nₐ", unitRes = R.string.mol_1, value = "6.022 × 10²³", nameRes = R.string.av_const),
                Item(symbol = "R", unitRes = R.string.j_mol_k, value = "8.314", nameRes = R.string.un_gas),
                Item(symbol = "Vₘ", unitRes = R.string.l_m, value = "22.414", nameRes = R.string.mol_vol),
                Item(symbol = "p₀", unitRes = R.string.pa, value = "101325", nameRes = R.string.atm_p),
                Item(symbol = "T₀", unitRes = R.string.k, value = "273.15", nameRes = R.string.nor_t)
            )
        )
    )
}