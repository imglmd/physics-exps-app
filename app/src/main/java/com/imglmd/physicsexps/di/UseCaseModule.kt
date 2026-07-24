package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.usecase.auth.EnsureAuthorizedUseCase
import com.imglmd.physicsexps.domain.usecase.auth.PingUseCase
import com.imglmd.physicsexps.domain.usecase.auth.RegisterUseCase
import com.imglmd.physicsexps.domain.usecase.comment.AddCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.core.experiments.usecase.GetAllExperimentsUseCase
import com.imglmd.core.experiments.usecase.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentPreviewsUseCase
import com.imglmd.physicsexps.domain.usecase.media.DeleteMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaSignedUrlUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.UploadMediaUseCase
import com.imglmd.feature.history.domain.usecase.DeleteAllRunsUseCase
import com.imglmd.feature.history.domain.usecase.DeleteRunUseCase
import com.imglmd.feature.history.domain.usecase.GetAllRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetFilteredRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetLastRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetResultUseCase
import com.imglmd.feature.history.domain.usecase.GetRunUseCase
import com.imglmd.feature.history.domain.usecase.SaveRunUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { PingUseCase(get()) }

    factory { SaveRunUseCase(get(), get(), get()) }

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

    factory { RegisterUseCase(get()) }
    factory { EnsureAuthorizedUseCase(get(),get(), get()) }

    factory { UploadMediaUseCase(get()) }
    factory { GetMediaUseCase(get(), get()) }
    factory { GetMediaSignedUrlUseCase(get(), get()) }
    factory { DeleteMediaUseCase(get()) }
}
