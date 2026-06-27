package com.imglmd.physicsexps.feature.constants.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.imglmd.physicsexps.feature.constants.R

@Composable
internal fun getStringByKey(key: String?): String {
    if (key.isNullOrBlank()) return ""
    return when (key) {
        "math_c" -> stringResource(R.string.math_c)
        "mechanics" -> stringResource(R.string.mechanics)
        "electricity" -> stringResource(R.string.electricity)
        "phonics" -> stringResource(R.string.phonics)
        "nuclear_physics" -> stringResource(R.string.nuclear_physics)
        "mol_ph" -> stringResource(R.string.mol_ph)
        "pi" -> stringResource(R.string.pi)
        "e" -> stringResource(R.string.e)
        "gravity" -> stringResource(R.string.gravity)
        "gravitational_constant" -> stringResource(R.string.gravitational_constant)
        "mass_earth" -> stringResource(R.string.mass_earth)
        "radius_earth" -> stringResource(R.string.radius_earth)
        "el_ch" -> stringResource(R.string.el_ch)
        "electron_mass" -> stringResource(R.string.electron_mass)
        "proton_mass" -> stringResource(R.string.proton_mass)
        "neutron_mass" -> stringResource(R.string.neutron_mass)
        "coulomb_constant" -> stringResource(R.string.coulomb_constant)
        "speed_0" -> stringResource(R.string.speed_0)
        "speed_20" -> stringResource(R.string.speed_20)
        "speed_w" -> stringResource(R.string.speed_w)
        "speed_g" -> stringResource(R.string.speed_g)
        "speed_s" -> stringResource(R.string.speed_s)
        "human" -> stringResource(R.string.human)
        "u" -> stringResource(R.string.u)
        "eq_atom" -> stringResource(R.string.eq_atom)
        "b_const" -> stringResource(R.string.b_const)
        "av_const" -> stringResource(R.string.av_const)
        "un_gas" -> stringResource(R.string.un_gas)
        "mol_vol" -> stringResource(R.string.mol_vol)
        "atm_p" -> stringResource(R.string.atm_p)
        "nor_t" -> stringResource(R.string.nor_t)
        "m_s_2" -> stringResource(R.string.m_s_2)
        "m_3_kg_s_2" -> stringResource(R.string.m_3_kg_s_2)
        "kg" -> stringResource(R.string.kg)
        "m" -> stringResource(R.string.m)
        "c" -> stringResource(R.string.c)
        "n_m_2_c_2" -> stringResource(R.string.n_m_2_c_2)
        "m_s" -> stringResource(R.string.m_s)
        "hz" -> stringResource(R.string.hz)
        "mev" -> stringResource(R.string.mev)
        "j_k" -> stringResource(R.string.j_k)
        "mol_1" -> stringResource(R.string.mol_1)
        "j_mol_k" -> stringResource(R.string.j_mol_k)
        "l_m" -> stringResource(R.string.l_m)
        "pa" -> stringResource(R.string.pa)
        "k" -> stringResource(R.string.k)
        else -> ""
    }
}
