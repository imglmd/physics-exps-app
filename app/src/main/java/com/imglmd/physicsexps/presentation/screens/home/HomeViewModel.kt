package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.domain.usecase.GetExperimentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {
    private val getExperimentsUseCase by lazy { GetExperimentsUseCase() }
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
}