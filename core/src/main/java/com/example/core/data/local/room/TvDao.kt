package com.example.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.data.local.entity.TvEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvDao {

    @Query("SELECT * FROM tv")
    fun getAllTv(): Flow<List<TvEntity>>

    @Query("""
        SELECT * FROM tv
        WHERE category = :category
    """)
    fun getTvsByCategory(
        category: String
    ): Flow<List<TvEntity>>

    @Query("""
        SELECT * FROM tv
        WHERE isFavorite = 1
        ORDER BY updatedAt DESC
    """)
    fun getFavoriteTv(): Flow<List<TvEntity>>

    @Query("""
        SELECT * FROM tv
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getTvById(id: Int): TvEntity?

    @Query("""
        SELECT * FROM tv
        WHERE id = :id
        LIMIT 1
    """)
    fun getFavoriteTvById(id: Int): Flow<TvEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTv(tv: List<TvEntity>)

    @Update
    suspend fun updateFavoriteTv(tv: TvEntity)
}