package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.database.repository.ExperimentRunsRepositoryImpl
import com.imglmd.physicsexps.data.database.repository.ResultsRepositoryImpl
import com.imglmd.physicsexps.data.remote.MediaRepositoryImpl
import com.imglmd.physicsexps.data.repositoryImpl.CommentRepositoryImpl
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.MediaRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { InMemoryResultRepository() }

    single<ResultsRepository> {
        ResultsRepositoryImpl(get())
    }

    single<ExperimentRunsRepository> {
        ExperimentRunsRepositoryImpl(get())
    }

    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single<MediaRepository> {
        MediaRepositoryImpl(get())
    }
}