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
import com.imglmd.feature.experiment.domain.usecase.comment.AddCommentUseCase
import com.imglmd.feature.experiment.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.feature.experiment.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.feature.experiment.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.feature.experiment.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.feature.experiment.domain.usecase.media.DeleteMediaUseCase
import com.imglmd.feature.experiment.domain.usecase.media.GetMediaSignedUrlUseCase
import com.imglmd.feature.experiment.domain.usecase.media.GetMediaUseCase
import com.imglmd.feature.experiment.domain.usecase.media.UploadMediaUseCase
import com.imglmd.feature.experiment.presentation.experiment.ExperimentViewModel
import com.imglmd.feature.experiment.presentation.result.ResultViewModel
import com.imglmd.feature.experiment.presentation.solution.SolutionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
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

    factory { AddCommentUseCase(get()) }
    factory { DeleteCommentUseCase(get()) }
    factory { GetCommentsUseCase(get()) }

    factory { CalculateExperimentUseCase(get(), get()) }
    factory { GetExperimentImagesUseCase(get()) }

    factory { DeleteMediaUseCase(get()) }
    factory { GetMediaSignedUrlUseCase(get(), get()) }
    factory { GetMediaUseCase(get(), get()) }
    factory { UploadMediaUseCase(get()) }

    factory { SaveRunUseCase(get(), get(), get()) }
    factory { GetRunUseCase(get()) }
    factory { GetResultUseCase(get()) }
    factory { GetAllRunsUseCase(get()) }
    factory { GetLastRunsUseCase(get()) }
    factory { DeleteRunUseCase(get()) }
    factory { DeleteAllRunsUseCase(get()) }

    viewModel { params ->

        val inputs: Map<String, String>? = params.getOrNull()
        val replaceRunId: Int? = params.getOrNull()

        ExperimentViewModel(
            id = params.get(),
            inputs = inputs,
            replaceRunId = replaceRunId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { params ->

        val runId: Int? = params.getOrNull()

        ResultViewModel(
            runId,
            get(),
            get(named("appScope")),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }


    viewModel {
        SolutionViewModel(get(), get())
    }
}