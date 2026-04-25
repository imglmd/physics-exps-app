package com.imglmd.physicsexps.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.database.ExpDb
import com.imglmd.physicsexps.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.physicsexps.data.repository.ResultsRepositoryImpl
import com.imglmd.physicsexps.data.repositoryImpl.CommentRepositoryImpl
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import com.imglmd.physicsexps.domain.validation.ExperimentValidator
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentViewModel
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import com.imglmd.physicsexps.presentation.screens.result.ResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { InMemoryResultRepository() }
    single<ResultsRepository> { ResultsRepositoryImpl(get()) }
    single { ExperimentValidator() }

    factory { SaveRunUseCase(get(), get()) }
    factory { DeleteRunUseCase(get(), get()) }
    factory { GetAllRunsUseCase(get()) }
    factory { GetAllExperimentsUseCase(get()) }
    factory { GetExperimentByIdUseCase(get()) }
    factory { CalculateExperimentUseCase(get(),get()) }
    viewModel {
        HomeViewModel(get(), get(), get())
    }
    viewModel { params ->
        ExperimentViewModel(params.get(), get(), get(), get())
    }
    viewModel {
        ResultViewModel(get(), get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            ExpDb::class.java,
            "exp_db"
        )
            //TODO: убрать ()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("""
                INSERT INTO experiment_runs (experiment_id, date, inputData, result_id)
                VALUES 
                ('pendulum', ${System.currentTimeMillis()}, '{"v": 10.0}', 0),
                ('free_fall', ${System.currentTimeMillis()}, '{"h": 20.0}', 0),
                ('projectile_motion', ${System.currentTimeMillis()}, '{"v": 20.0}', 1)
                
            """.trimIndent())
                }
            })
            .build()
    }
    single { get<ExpDb>().resDao() }
    single{get<ExpDb>().dao()}
    single{get<ExpDb>().comDao()}
    single<ExperimentRunsRepository> { ExperimentRunsRepositoryImpl(get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
}