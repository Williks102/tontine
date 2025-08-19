package com.tontinepro.di

import android.content.Context
import androidx.room.Room
import com.tontinepro.data.local.dao.BulleDao
import com.tontinepro.data.local.dao.TransactionDao
import com.tontinepro.data.local.dao.UserDao
import com.tontinepro.data.local.database.TontineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TontineDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TontineDatabase::class.java,
            "tontine_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: TontineDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideBulleDao(database: TontineDatabase): BulleDao {
        return database.bulleDao()
    }

    @Provides
    fun provideTransactionDao(database: TontineDatabase): TransactionDao {
        return database.transactionDao()
    }
}
