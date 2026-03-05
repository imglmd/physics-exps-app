@file:OptIn(KoinExperimentalAPI::class)

package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.presentation.navigation.Navigator
import com.imglmd.physicsexps.presentation.navigation.Screen
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentScreen
import com.imglmd.physicsexps.presentation.screens.home.HomeScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val navigationModule = module {
    single { Navigator(startDestination = Screen.Home) }
    navigation<Screen.Home> {
        HomeScreen()
    }
    navigation<Screen.Experiment> { route ->
        ExperimentScreen(
            id = route.id,
            navigateBack = {
                get<Navigator>().goBack()
            }
        )
    }
}