package com.tontinepro.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.tontinepro.data.local.dao.BulleDao
import com.tontinepro.data.local.dao.TransactionDao
import com.tontinepro.data.local.dao.UserDao
import com.tontinepro.data.local.entities.BulleEntity
import com.tontinepro.data.local.entities.TransactionEntity
import com.tontinepro.data.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        BulleEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TontineDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bulleDao(): BulleDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TontineDatabase? = null

        fun getDatabase(context: Context): TontineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TontineDatabase::class.java,
                    "tontine_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}