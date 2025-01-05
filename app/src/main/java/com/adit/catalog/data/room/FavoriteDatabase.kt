package com.adit.catalog.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ObjekDataEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao


}
