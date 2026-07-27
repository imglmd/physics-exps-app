package com.imglmd.feature.constants.domain.usecase

import com.imglmd.feature.constants.data.PhysicsConstantsProvider
import com.imglmd.feature.constants.domain.model.Category

class GetAllCategoriesUseCase {
    operator fun invoke(): List<Category> = PhysicsConstantsProvider.provide()
}