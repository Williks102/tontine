package com.tontinepro.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tontinepro.BuildConfig
import com.tontinepro.data.remote.api.*
import com.tontinepro.data.remote.interceptors.AuthInterceptor
import com.tontinepro.data.remote.interceptors.NetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {