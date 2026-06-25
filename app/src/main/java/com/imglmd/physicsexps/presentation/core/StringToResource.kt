package com.imglmd.physicsexps.presentation.core

import android.annotation.SuppressLint
import android.content.Context
import com.imglmd.physicsexps.R

fun Context.getStringByKey(key: String?): String {
    if (key.isNullOrBlank()) return ""
    return when(key) {
        "main" -> getString(R.string.main)
        "settings" -> getString(R.string.settings)
        "coulombs_law" -> getString(R.string.coulombs_law)
        "electricity" -> getString(R.string.electricity)
        "coulombs_law_desc" -> getString(R.string.coulombs_law_desc)
        "q1_10_9" -> getString(R.string.q1_10_9)
        "q2_10_9" -> getString(R.string.q2_10_9)
        "col_dist" -> getString(R.string.col_dist)
        "dist" -> getString(R.string.dist)
        "nano" -> getString(R.string.nano)
        "doppler_effect" -> getString(R.string.doppler_effect)
        "phonics" -> getString(R.string.phonics)
        "doppler_desc" -> getString(R.string.doppler_desc)
        "free_fall" -> getString(R.string.free_fall)
        "kinematics" -> getString(R.string.kinematics)
        "free_fall_desc" -> getString(R.string.free_fall_desc)
        "harmonic_vibrations" -> getString(R.string.harmonic_vibrations)
        "mechanics" -> getString(R.string.mechanics)
        "harmonic_vibrations_desc" -> getString(R.string.harmonic_vibrations_desc)
        "joule_lenz" -> getString(R.string.joule_lenz)
        "joule_lenz_desc" -> getString(R.string.joule_lenz_desc)
        "pendulum_desc" -> getString(R.string.pendulum_desc)
        "pendulum" -> getString(R.string.pendulum)
        "physical_pendulum_desc" -> getString(R.string.physical_pendulum_desc)
        "physical_pendulum" -> getString(R.string.physical_pendulum)
        "projectile_motion_desc" -> getString(R.string.projectile_motion_desc)
        "projectile_motion" -> getString(R.string.projectile_motion)
        "radioactive_decay_desc" -> getString(R.string.radioactive_decay_desc)
        "radioactive_decay" -> getString(R.string.radioactive_decay)
        "nuclear_physics" -> getString(R.string.nuclear_physics)
        "spring_pendulum_desc" -> getString(R.string.spring_pendulum_desc)
        "spring_pendulum" -> getString(R.string.spring_pendulum)
        else -> ""
    }
}