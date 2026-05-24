package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.usecase.comment.AddCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentPreviewsUseCase
import com.imglmd.physicsexps.domain.usecase.media.DeleteMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.UploadMediaUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetFilteredRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetLastRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { SaveRunUseCase(get(), get()) }

    factory { DeleteRunUseCase(get()) }

    factory { DeleteAllRunsUseCase(get()) }

    factory { GetAllRunsUseCase(get()) }

    factory { GetRunUseCase(get()) }

    factory { GetResultUseCase(get()) }

    factory { GetLastRunsUseCase(get()) }

    factory { GetFilteredRunsUseCase(get(), get()) }

    factory { GetAllExperimentsUseCase(get()) }

    factory { GetExperimentByIdUseCase(get()) }

    factory { GetExperimentPreviewsUseCase(get()) }

    factory { GetExperimentImagesUseCase(get()) }

    factory { CalculateExperimentUseCase(get(), get()) }

    factory { AddCommentUseCase(get()) }

    factory { DeleteCommentUseCase(get()) }

    factory { GetCommentsUseCase(get()) }

    factory { UploadMediaUseCase(get()) }
    factory { GetMediaUseCase(get()) }
    factory { DeleteMediaUseCase(get()) }
}
