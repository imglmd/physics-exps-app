package com.imglmd.physicsexps.core.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.imglmd.physicsexps.core.ui.R

//TODO вынести сюда все общие иконки и использовать их в приложении
/**
 * тут короче хранятся общие иконки для всех модулей
 */
object AppIcons {
    val ChevronRight @Composable get() =
        ImageVector.vectorResource(R.drawable.chevron_right)
    val ChevronLeft @Composable get() =
        ImageVector.vectorResource(R.drawable.chevron_left)
    val ArrowDropDown @Composable get() =
        ImageVector.vectorResource(R.drawable.arrow_drop_down)
    val ArrowBack @Composable get() =
        ImageVector.vectorResource(R.drawable.arrow_back)
    val Search @Composable get() =
        ImageVector.vectorResource(R.drawable.search)
    val Tune @Composable get() =
        ImageVector.vectorResource(R.drawable.tune)
    val Rocket @Composable get() =
        ImageVector.vectorResource(R.drawable.rocket_filled)
    val RocketOutlined @Composable get() =
        ImageVector.vectorResource(R.drawable.rocket_outlined)
}