package com.example.tmdbapp.di

import com.example.core.domain.usecase.movie.MovieInteractor
import com.example.core.domain.usecase.movie.MovieUseCase
import com.example.core.domain.usecase.movie.detail.MovieDetailInteractor
import com.example.core.domain.usecase.movie.detail.MovieDetailUseCase
import com.example.core.domain.usecase.tv.TvInteractor
import com.example.core.domain.usecase.tv.TvUseCase
import com.example.core.domain.usecase.tv.detail.TvDetailInteractor
import com.example.core.domain.usecase.tv.detail.TvDetailUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideMovieUseCase(
        movieInteractor: MovieInteractor
    ): MovieUseCase

    @Binds
    @Singleton
    abstract fun provideMovieDetailUseCase(
        movieDetailInteractor: MovieDetailInteractor
    ): MovieDetailUseCase

    @Binds
    @Singleton
    abstract fun provideTvUseCase(
        tvInteractor: TvInteractor
    ): TvUseCase

    @Binds
    @Singleton
    abstract fun provideTvDetailUseCase(
        tvDetailInteractor: TvDetailInteractor
    ): TvDetailUseCase
}