package com.imglmd.physicsexps.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imglmd.physicsexps.data.database.models.CommentEntity
import com.imglmd.physicsexps.data.database.models.ExperimentRunEntity
import com.imglmd.physicsexps.data.database.models.ResultEntity


@Database(
    version = 1,
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