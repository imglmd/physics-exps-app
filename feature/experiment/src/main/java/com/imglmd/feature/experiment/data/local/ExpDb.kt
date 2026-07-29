package com.imglmd.feature.experiment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imglmd.feature.experiment.data.local.models.CommentEntity


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