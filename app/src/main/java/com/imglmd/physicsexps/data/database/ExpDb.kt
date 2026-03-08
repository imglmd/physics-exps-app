package com.imglmd.physicsexps.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.imglmd.physicsexps.data.database.entities.ExperimentEntity
import com.imglmd.physicsexps.data.database.entities.ExperimentRunEntity
import com.imglmd.physicsexps.data.database.entities.ResultEntity

@Database(
    version = 1,
    entities = [
        ExperimentRunEntity::class,
        ExperimentEntity::class,
        ResultEntity::class
    ]
)
abstract class ExpDb: RoomDatabase() {
    abstract val dao: ExperimentRunsDao

    companion object {
        fun creteDataBase(context: Context): ExpDb {
            return Room.databaseBuilder(
                context,
                ExpDb::class.java,
                "experiment_runs.db"
            ).build()
        }
    }
}