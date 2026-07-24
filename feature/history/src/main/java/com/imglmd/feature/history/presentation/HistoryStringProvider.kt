package com.imglmd.feature.history.presentation

import com.imglmd.feature.history.R
import com.imglmd.physicsexps.core.ui.utils.StringKeyProvider

class HistoryStringProvider : StringKeyProvider {
    override fun resolve(key: String): Int? = when (key) {
        "select_exps" -> R.string.select_exps
        "delete_history" -> R.string.delete_history
        "no_exps" -> R.string.no_exps

        else -> null
    }
}