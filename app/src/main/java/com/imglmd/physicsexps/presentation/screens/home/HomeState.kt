package com.imglmd.physicsexps.presentation.screens.home

import com.imglmd.physicsexps.domain.model.Experiment

data class HomeState(
    val experiments: List<Experiment> = emptyList(),
    val history: List<Nothing> = emptyList(), // TODO
    val searchText: String = ""
)