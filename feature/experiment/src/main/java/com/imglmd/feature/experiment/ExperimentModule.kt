package com.imglmd.feature.experiment

import com.imglmd.feature.experiment.data.InMemoryResultRepository
import com.imglmd.feature.experiment.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.feature.experiment.data.repository.ResultsRepositoryImpl
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository
import com.imglmd.feature.experiment.domain.repository.ResultsRepository
import com.imglmd.feature.experiment.domain.usecase.DeleteAllRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.DeleteRunUseCase
import com.imglmd.feature.experiment.domain.usecase.GetAllRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.GetLastRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.GetResultUseCase
import com.imglmd.feature.experiment.domain.usecase.GetRunUseCase
import com.imglmd.feature.experiment.domain.usecase.SaveRunUseCase
import org.koin.dsl.module

val experimentFeatModule = module {
    single { InMemoryResultRepository() }

    single<ExperimentRunsRepository> { ExperimentRunsRepositoryImpl(get()) }
    single<ResultsRepository> { ResultsRepositoryImpl(get()) }

    factory { SaveRunUseCase(get(), get(), get()) }
    factory { GetRunUseCase(get()) }
    factory { GetResultUseCase(get()) }
    factory { GetAllRunsUseCase(get()) }
    factory { GetLastRunsUseCase(get()) }
    factory { DeleteRunUseCase(get()) }
    factory { DeleteAllRunsUseCase(get()) }
}