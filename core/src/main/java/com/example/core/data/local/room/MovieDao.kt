package com.example.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllMovie(): Flow<List<MovieEntity>>

    @Query("""
        SELECT * FROM movie
        WHERE category = :category
    """)
    fun getMoviesByCategory(
        category: String
    ): Flow<List<MovieEntity>>

    @Query("""
        SELECT * FROM movie
        WHERE isFavorite = 1
        ORDER BY updatedAt DESC
    """)
    fun getFavoriteMovie(): Flow<List<MovieEntity>>

    @Query("""
        SELECT * FROM movie
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("""
        SELECT * FROM movie
        WHERE id = :id
        LIMIT 1
    """)
    fun getFavoriteMovieById(id: Int): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: List<MovieEntity>)

    @Update
    suspend fun updateFavoriteMovie(movie: MovieEntity)
}