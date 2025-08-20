package com.example.gameofthrones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.example.gameofthrones.data.room.AppDatabase
import com.example.gameofthrones.data.room.FavoriteDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration() // para desarrollo; en producción manejá migraciones
            .build()
    }

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}
