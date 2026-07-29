package com.imglmd.physicsexps.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imglmd.feature.experiment.data.local.ExperimentRunEntity
import com.imglmd.feature.experiment.data.local.ExperimentRunsDao
import com.imglmd.feature.experiment.data.local.ResultEntity
import com.imglmd.feature.experiment.data.local.ResultsDao
import com.imglmd.physicsexps.data.database.models.CommentEntity


@Database(
    version = 2,
    entities = [
        ExperimentRunEntity::class,
        ResultEntity::class,
        CommentEntity::class
    ]
)
abstract class ExpDb: RoomDatabase() {
    abstract fun dao(): ExperimentRunsDao
    abstract fun comDao(): CommentsDao
    abstract fun resDao(): ResultsDao
}