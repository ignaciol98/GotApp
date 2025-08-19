package com.example.gameofthrones.di

import android.content.Context
import com.example.gameofthrones.data.AssetHousesRepository
import com.example.gameofthrones.model.HousesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHousesRepository(
        @ApplicationContext context: Context
    ): HousesRepository {
        // Le decimos a Hilt que use AssetHousesRepository
        return AssetHousesRepository(context)
    }
}