package com.adit.catalog.di

import android.content.Context
import androidx.room.Room
import com.adit.catalog.data.room.FavoriteDao
import com.adit.catalog.data.room.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "catalog_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: FavoriteDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}
