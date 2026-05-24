package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.repository.AuthRepositoryImpl
import com.imglmd.physicsexps.data.repository.CommentRepositoryImpl
import com.imglmd.physicsexps.data.repository.ExperimentMediaRepositoryImpl
import com.imglmd.physicsexps.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.physicsexps.data.repository.MediaRepositoryImpl
import com.imglmd.physicsexps.data.repository.ResultsRepositoryImpl
import com.imglmd.physicsexps.domain.repository.AuthRepository
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentMediaRepository
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.MediaRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { InMemoryResultRepository() }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    single<ResultsRepository> {
        ResultsRepositoryImpl(get())
    }

    single<ExperimentRunsRepository> {
        ExperimentRunsRepositoryImpl(get())
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
