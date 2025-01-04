package com.adit.catalog.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorites")
data class ObjekDataEntity(
    @PrimaryKey val id: Int,
    val image: Int,
    val title: String,
    val sinopsis: String,
    val thick: String,
    val price: Int,
    val category: String,
    val author: String,
    val published: String,
    val isFavorite: Boolean = false
) : Serializable