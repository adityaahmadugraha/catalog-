package com.adit.catalog.data.local

import android.os.Parcelable
import com.adit.catalog.data.room.ObjekDataEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObjekData(
    val id: Int,
    val image: Int,
    val title: String,
    val sinopsis: String,
    val thick: String,
    val price: Int,
    val category: String,
    val author: String,
    val published: String,
    val isFavorite: Boolean = false
) : Parcelable {

    fun toEntity(): ObjekDataEntity {
        return ObjekDataEntity(
            id = id,
            image = image,
            title = title,
            sinopsis = sinopsis,
            thick = thick,
            price = price,
            category = category,
            author = author,
            published = published,
            isFavorite = isFavorite
        )
    }
}

