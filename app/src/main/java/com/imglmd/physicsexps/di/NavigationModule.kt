@file:OptIn(KoinExperimentalAPI::class)

package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.presentation.navigation.HistoryMode
import com.imglmd.physicsexps.presentation.navigation.Navigator
import com.imglmd.physicsexps.presentation.navigation.Screen
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentScreen
import com.imglmd.physicsexps.presentation.screens.history.HistoryScreen
import com.imglmd.physicsexps.presentation.screens.home.HomeScreen
import com.imglmd.physicsexps.presentation.screens.result.FullScreenChartScreen
import com.imglmd.physicsexps.presentation.screens.result.ResultScreen
import com.imglmd.physicsexps.presentation.screens.solution.SolutionScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

val navigationModule = module {
    single { Navigator(startDestination = Screen.Home) }
    navigation<Screen.Home> {
        HomeScreen(
            navigateToExperiment = { id ->
                get<Navigator>().navigateTo(Screen.Experiment(id))
            },
            navigateToResult = { runId ->
                get<Navigator>().navigateTo(Screen.Result(runId))
            },
            navigateToHistory = {
                get<Navigator>().navigateTo(Screen.History())
            }
        )
    }
    navigation<Screen.Experiment> { route ->
        ExperimentScreen(
            id = route.id,
            inputs = route.inputs,
            replaceRunId = route.replaceRunId,
            navigateBack = {
                get<Navigator>().goBack()
            },
            navigateToResult = {
                get<Navigator>().navigateTo(Screen.Result())
            }
        )
    }
    navigation<Screen.Result> { route ->
        ResultScreen(
            runId = route.runId,
            navigateBack = { get<Navigator>().goBack() },
            navigateHome = { get<Navigator>().goHome() },
            navigateExperiment = { expId, inputs, replaceRunId ->
                get<Navigator>().replaceTo(Screen.Experiment(expId, inputs, replaceRunId))
            },
            navigateChart = { runId ->
                get<Navigator>().navigateTo(Screen.FullScreenChart(runId))
            },
            navigateSolution = { get<Navigator>().navigateTo(Screen.Solution) },
            navigateCompare = {  id ->
                get<Navigator>().navigateTo(Screen.History(mode = HistoryMode.SELECTION, listOf(id)))
            }
        )
    }
    navigation<Screen.History>{ route ->
        HistoryScreen(
            mode = route.mode,
            preselectedIds = route.preselectedIds,
            navigateBack = { get<Navigator>().goBack() },
            navigateToResult = { runId ->
                get<Navigator>().navigateTo(Screen.Result(runId))
            },
            onSelectRuns = {
                TODO()
            }
        )
    }
    navigation<Screen.FullScreenChart> { route ->
        FullScreenChartScreen(
            runId = route.runId,
            navigateBack = { get<Navigator>().goBack() }
        )
    }
    navigation<Screen.Solution> {
        SolutionScreen(
            navigateBack = { get<Navigator>().goBack() }
        )
    }
}