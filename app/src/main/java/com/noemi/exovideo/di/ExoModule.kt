package com.noemi.exovideo.di

import android.content.Context
import com.noemi.exovideo.provider.RecipesProvider
import com.noemi.exovideo.provider.RecipesProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExoModule {

    @Provides
    @Singleton
    fun providesReceipt(@ApplicationContext context: Context): RecipesProvider = RecipesProviderImpl(context)
}