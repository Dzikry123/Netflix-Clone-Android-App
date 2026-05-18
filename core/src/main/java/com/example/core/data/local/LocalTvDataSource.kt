package com.example.core.data.local

import com.example.core.data.local.entity.TvEntity
import com.example.core.data.local.room.TvDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTvDataSource @Inject constructor(
    private val tvDao: TvDao
) {

    fun getAllTv(): Flow<List<TvEntity>> =
        tvDao.getAllTv()

    fun getTvsByCategory(category: String): Flow<List<TvEntity>> =
        tvDao.getTvsByCategory(category)

    fun getFavoriteTv(): Flow<List<TvEntity>> =
        tvDao.getFavoriteTv()

    fun getFavoriteTvById(id: Int): Flow<TvEntity?> =
        tvDao.getFavoriteTvById(id)

    suspend fun getTvById(id: Int): TvEntity? =
        tvDao.getTvById(id)

    suspend fun insertTv(tvList: List<TvEntity>) =
        tvDao.insertTv(tvList)

    suspend fun setFavoriteTv(tvId: Int, newState: Boolean) {

        val tv = tvDao.getTvById(tvId)
            ?: return

        tv.isFavorite = newState
        tv.updatedAt = System.currentTimeMillis()

        tvDao.updateFavoriteTv(tv)
    }
}