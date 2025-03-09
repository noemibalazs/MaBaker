package com.noemi.exovideo.di

import android.content.Context
import com.noemi.exovideo.repository.RecipeRepository
import com.noemi.exovideo.repository.RecipeRepositoryImpl
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
    fun providesRepository(@ApplicationContext context: Context): RecipeRepository = RecipeRepositoryImpl(context)
}