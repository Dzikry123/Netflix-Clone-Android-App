package com.example.core.dynamicfeature

import com.example.core.domain.usecase.movie.MovieUseCase
import com.example.core.domain.usecase.tv.TvUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteEntryPoint {

    fun movieUseCase(): MovieUseCase
    fun tvUseCase(): TvUseCase
}

