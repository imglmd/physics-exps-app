package com.imglmd.physicsexps.feature.constants.data

import com.imglmd.physicsexps.feature.constants.R
import com.imglmd.physicsexps.feature.constants.domain.model.Category
import com.imglmd.physicsexps.feature.constants.domain.model.Constant
import com.imglmd.physicsexps.feature.constants.domain.model.NumberValue

internal object PhysicsConstantsProvider {

    fun provide(): List<Category> = listOf(
        Category(
            icon = R.drawable.math,
            labelRes = R.string.math_c,
            listConstant = listOf(
                Constant(symbol = "π", value = NumberValue.Single(3.14159265359), nameRes = R.string.pi),
                Constant(symbol = "e", value = NumberValue.Single(2.71828182846), nameRes = R.string.e)
            )
        ),
        Category(
            icon = R.drawable.mechenics_icon,
            labelRes = R.string.mechanics,
            listConstant = listOf(
                Constant(symbol = "g", unitRes = R.string.m_s_2, value =  NumberValue.Single(9.80665), nameRes = R.string.gravity),
                Constant(symbol = "G", unitRes = R.string.m_3_kg_s_2, value =  NumberValue.Single(6.6743E-11), nameRes = R.string.gravitational_constant),
                Constant(symbol = "M⊕", unitRes = R.string.kg, value = NumberValue.Single(5.9722E24), nameRes = R.string.mass_earth),
                Constant(symbol = "R⊕", unitRes = R.string.m, value = NumberValue.Single(6371.0 * 1000), nameRes = R.string.radius_earth)
            )
        ),
        Category(
            icon = R.drawable.light,
            labelRes = R.string.electricity,
            listConstant = listOf(
                Constant(symbol = "e", unitRes = R.string.c, value = NumberValue.Single( 1.60218E-19), nameRes = R.string.el_ch),
                Constant(symbol = "mₑ", unitRes = R.string.kg, value = NumberValue.Single(9.10938E-31), nameRes = R.string.electron_mass),
                Constant(symbol = "mₚ", unitRes = R.string.kg, value = NumberValue.Single(1.6726219E-27), nameRes = R.string.proton_mass),
                Constant(symbol = "mₙ", unitRes = R.string.kg, value = NumberValue.Single(1.67492749E-27), nameRes = R.string.neutron_mass),
                Constant(symbol = "k", unitRes = R.string.n_m_2_c_2, value = NumberValue.Single(8.98755E9), nameRes = R.string.coulomb_constant)
            )
        ),
        Category(
            icon = R.drawable.sound,
            labelRes = R.string.phonics,
            listConstant = listOf(
                Constant(symbol = "c", unitRes = R.string.m_s, value = NumberValue.Single(331.0), nameRes = R.string.speed_0),
                Constant(symbol = "c", unitRes = R.string.m_s, value = NumberValue.Single(343.0), nameRes = R.string.speed_20),
                Constant(symbol = "c", unitRes = R.string.m_s, value = NumberValue.Single(1480.0), nameRes = R.string.speed_w),
                Constant(symbol = "c", unitRes = R.string.m_s, value = NumberValue.Single(5500.0), nameRes = R.string.speed_g),
                Constant(symbol = "c", unitRes = R.string.m_s, value = NumberValue.Range(5000.0, 6100.0), nameRes = R.string.speed_s),
                Constant(symbol = "f", unitRes = R.string.hz, value = NumberValue.Range(20.0, 20_000.0), nameRes = R.string.human)
            )
        ),
        Category(
            icon = R.drawable.orbit,
            labelRes = R.string.nuclear_physics,
            listConstant = listOf(
                Constant(symbol = "u", unitRes = R.string.kg, value = NumberValue.Single(1.66054E-27), nameRes = R.string.u),
                Constant(symbol = "E", unitRes = R.string.mev, value = NumberValue.Single(931.5), nameRes = R.string.eq_atom),
                Constant(symbol = "mₑ", unitRes = R.string.kg, value = NumberValue.Single(9.10938E-31), nameRes = R.string.electron_mass),
                Constant(symbol = "mₚ", unitRes = R.string.kg, value = NumberValue.Single(1.67262E-27), nameRes = R.string.proton_mass),
                Constant(symbol = "mₙ", unitRes = R.string.kg, value = NumberValue.Single(1.67493E-27), nameRes = R.string.neutron_mass)
            )
        ),
        Category(
            icon = R.drawable.mol,
            labelRes = R.string.mol_ph,
            listConstant = listOf(
                Constant(symbol = "k", unitRes = R.string.j_k, value = NumberValue.Single(1.381E-23), nameRes = R.string.b_const),
                Constant(symbol = "Nₐ", unitRes = R.string.mol_1, value = NumberValue.Single(6.022E23), nameRes = R.string.av_const),
                Constant(symbol = "R", unitRes = R.string.j_mol_k, value = NumberValue.Single(8.314), nameRes = R.string.un_gas),
                Constant(symbol = "Vₘ", unitRes = R.string.l_m, value = NumberValue.Single(22.414), nameRes = R.string.mol_vol),
                Constant(symbol = "p₀", unitRes = R.string.pa, value = NumberValue.Single(101325.0), nameRes = R.string.atm_p),
                Constant(symbol = "T₀", unitRes = R.string.k, value = NumberValue.Single(273.15), nameRes = R.string.nor_t)
            )
        )
    )
}