package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.data.repository.CommentRepositoryImpl
import com.imglmd.physicsexps.data.repository.ExperimentMediaRepositoryImpl
import com.imglmd.physicsexps.data.repository.MediaRepositoryImpl
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentMediaRepository
import com.imglmd.physicsexps.domain.repository.MediaRepository
import org.koin.dsl.module

val repositoryModule = module {

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
