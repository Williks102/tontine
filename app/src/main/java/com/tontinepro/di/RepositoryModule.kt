package com.tontinepro.di

import com.tontinepro.data.local.dao.*
import com.tontinepro.data.remote.api.*
import com.tontinepro.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        userDao: UserDao
    ): AuthRepository {
        return AuthRepository(authApiService, userDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        userDao: UserDao
    ): UserRepository {
        return UserRepository(userApiService, userDao)
    }

    @Provides
    @Singleton
    fun provideBulleRepository(
        bulleApiService: BulleApiService,
        bulleDao: BulleDao
    ): BulleRepository {
        return BulleRepository(bulleApiService, bulleDao)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        paymentApiService: PaymentApiService,
        transactionDao: TransactionDao
    ): PaymentRepository {
        return PaymentRepository(paymentApiService, transactionDao)
    }

    @Provides
    @Singleton
    fun provideFeteRepository(
        feteApiService: FeteApiService
    ): FeteRepository {
        return FeteRepository(feteApiService)
    }
}
