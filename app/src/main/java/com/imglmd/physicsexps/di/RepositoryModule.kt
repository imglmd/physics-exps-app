package com.imglmd.physicsexps.di

import com.imglmd.feature.history.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.repository.AuthRepositoryImpl
import com.imglmd.physicsexps.data.repository.CommentRepositoryImpl
import com.imglmd.physicsexps.data.repository.ExperimentMediaRepositoryImpl
import com.imglmd.feature.history.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.physicsexps.data.repository.MediaRepositoryImpl
import com.imglmd.feature.history.data.repository.ResultsRepositoryImpl
import com.imglmd.physicsexps.domain.repository.AuthRepository
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentMediaRepository
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.MediaRepository
import com.imglmd.feature.history.domain.repository.ResultsRepository
import org.koin.dsl.module

val repositoryModule = module {


    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    single<ExperimentMediaRepository> {
        ExperimentMediaRepositoryImpl(get(), get())
    }

    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single<MediaRepository> {
        MediaRepositoryImpl(get())
    }
}
