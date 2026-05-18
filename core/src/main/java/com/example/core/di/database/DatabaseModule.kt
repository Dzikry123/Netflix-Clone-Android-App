package com.example.core.di.database

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.room.MovieDao
import com.example.core.data.local.room.TmdbDatabase
import com.example.core.data.local.room.TvDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TmdbDatabase = Room.databaseBuilder(
        context,
        TmdbDatabase::class.java, "Tmdb.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideMovieDao(database: TmdbDatabase): MovieDao = database.movieDao()

    @Provides
    fun provideTvDao(database: TmdbDatabase): TvDao = database.tvDao()

}