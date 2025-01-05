package com.adit.catalog.data.repository

import androidx.lifecycle.LiveData
import com.adit.catalog.data.local.Data
import com.adit.catalog.data.room.FavoriteDao
import com.adit.catalog.data.room.ObjekDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    fun getListBook() = Data.ListDataLocal


    suspend fun removeFromFavorites(objekData: ObjekDataEntity) {
        withContext(Dispatchers.IO) {
            favoriteDao.delete(objekData)
        }
    }

    fun getFavoriteBooks() = favoriteDao.getAllFavorites()

    suspend fun addToFavorites(objekData: ObjekDataEntity) {

        val updatedObjekData = objekData.copy(isFavorite = true)
        withContext(Dispatchers.IO) {
            favoriteDao.insert(updatedObjekData)
        }
    }

    fun isFavorite(id: Int): LiveData<Boolean> {
        return favoriteDao.isFavorite(id)
    }

}
