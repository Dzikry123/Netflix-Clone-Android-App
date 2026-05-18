package com.example.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv")
data class TvEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "backgroundPath")
    val backdropPath: String,

    @ColumnInfo(name = "firstAirDate")
    val firstAirDate: String,

    @ColumnInfo(name = "genreIds")
    val genreIds: List<Int>,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "originalCountry")
    val originCountry: String,

    @ColumnInfo(name = "originalLanguage")
    val originalLanguage: String,

    @ColumnInfo(name = "originalName")
    val originalName: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "popularity")
    val popularity: Double,

    @ColumnInfo(name = "posterPath")
    val posterPath: String,

    @ColumnInfo(name = "softcore")
    val softcore: Boolean,

    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double,

    @ColumnInfo(name = "voteCount")
    val voteCount: Int,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: Long = System.currentTimeMillis()
)