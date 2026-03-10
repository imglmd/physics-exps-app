package com.imglmd.physicsexps.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.imglmd.physicsexps.data.database.models.ExperimentRunEntity
import com.imglmd.physicsexps.data.database.models.ResultEntity


@Database(
    version = 1,
    entities = [
        ExperimentRunEntity::class,
        ResultEntity::class
    ]
)
abstract class ExpDb: RoomDatabase() {
    abstract fun dao(): ExperimentRunsDao

    companion object {
        @Volatile
        private var INSTANCE: ExpDb? = null

        fun getInstance(context: Context): ExpDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpDb::class.java,
                    "your_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}