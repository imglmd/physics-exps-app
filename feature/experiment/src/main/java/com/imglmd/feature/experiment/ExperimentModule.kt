package com.imglmd.feature.experiment

import androidx.room.Room
import com.imglmd.feature.experiment.data.InMemoryResultRepository
import com.imglmd.feature.experiment.data.local.ExpDb
import com.imglmd.feature.experiment.data.repository.CommentRepositoryImpl
import com.imglmd.feature.experiment.data.repository.ExperimentMediaRepositoryImpl
import com.imglmd.feature.experiment.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.feature.experiment.data.repository.MediaRepositoryImpl
import com.imglmd.feature.experiment.data.repository.ResultsRepositoryImpl
import com.imglmd.feature.experiment.domain.repository.CommentRepository
import com.imglmd.feature.experiment.domain.repository.ExperimentMediaRepository
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository
import com.imglmd.feature.experiment.domain.repository.MediaRepository
import com.imglmd.feature.experiment.domain.repository.ResultsRepository
import com.imglmd.feature.experiment.domain.usecase.DeleteAllRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.DeleteRunUseCase
import com.imglmd.feature.experiment.domain.usecase.GetAllRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.GetLastRunsUseCase
import com.imglmd.feature.experiment.domain.usecase.GetResultUseCase
import com.imglmd.feature.experiment.domain.usecase.GetRunUseCase
import com.imglmd.feature.experiment.domain.usecase.SaveRunUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val experimentFeatModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ExpDb::class.java,
            "exp_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    single { get<ExpDb>().dao() }

    single { get<ExpDb>().resDao() }

    single { get<ExpDb>().comDao() }


    single<ExperimentMediaRepository> { ExperimentMediaRepositoryImpl(get(), get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<MediaRepository> { MediaRepositoryImpl(get()) }
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