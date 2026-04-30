package com.imglmd.physicsexps.presentation.screens.compare

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase

class CompareViewModel(
    private val runIds: List<Int>,
    private val getResultUseCase: GetResultUseCase
): ViewModel() {


}