package com.adit.catalog.data.room


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: ObjekDataEntity)

    @Query("SELECT * FROM favorites WHERE isFavorite = 1")
    fun getAllFavorites(): LiveData<List<ObjekDataEntity>>

    @Delete
    suspend fun delete(favorite: ObjekDataEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id AND isFavorite = 1)")
    fun isFavorite(id: Int): LiveData<Boolean>
}