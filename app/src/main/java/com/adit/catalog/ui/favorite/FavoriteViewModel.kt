package com.adit.catalog.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adit.catalog.data.repository.Repository
import com.adit.catalog.data.room.ObjekDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val favoriteBooks: LiveData<List<ObjekDataEntity>> = repository.getFavoriteBooks()

    fun removeFavorite(book: ObjekDataEntity) {
        viewModelScope.launch {
            repository.removeFromFavorites(book)
        }
    }

}
