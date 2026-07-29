package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.usecase.auth.EnsureAuthorizedUseCase
import com.imglmd.physicsexps.domain.usecase.auth.PingUseCase
import com.imglmd.physicsexps.domain.usecase.auth.RegisterUseCase
import com.imglmd.physicsexps.domain.usecase.comment.AddCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.DeleteCommentUseCase
import com.imglmd.physicsexps.domain.usecase.comment.GetCommentsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentImagesUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentPreviewsUseCase
import com.imglmd.physicsexps.domain.usecase.media.DeleteMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaSignedUrlUseCase
import com.imglmd.physicsexps.domain.usecase.media.GetMediaUseCase
import com.imglmd.physicsexps.domain.usecase.media.UploadMediaUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { PingUseCase(get()) }

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
