package com.imglmd.physicsexps.feature.constants.domain.usecase

import com.imglmd.physicsexps.feature.constants.data.PhysicsConstantsProvider
import com.imglmd.physicsexps.feature.constants.domain.model.Category

class GetAllCategoriesUseCase {
    operator fun invoke(): List<Category> = PhysicsConstantsProvider.provide()
}